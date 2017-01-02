package hr.foi.varazdinevents.places.newEvent;

import android.net.Uri;

import org.json.JSONException;

import java.io.File;
import java.net.UnknownHostException;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.responses.ErrorResponseComplete;
import hr.foi.varazdinevents.api.responses.ImgurResponse;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import hr.foi.varazdinevents.util.FileUtils;
import rx.Observer;
import timber.log.Timber;

/**
 * Created by Antonio Martinović on 03.12.16.
 */
public class NewEventPresenter extends BasePresenter<NewEventActivity> {

    private final EventManager eventManager;

    public NewEventPresenter(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public void itemClicked(Object item) {

    }

    public void uploadImage(final Event event, Uri image) {
        File imageFile = null;
        if(image != null) {
            imageFile = new File(FileUtils.getPath(getViewLayer(), image));
        }


        checkViewAttached();
        getViewLayer().showLoading(true);

        Observer<ImgurResponse> eventObserver = new Observer<ImgurResponse>() {
            @Override
            public void onNext(ImgurResponse imgurResponse) {

                event.setImage(imgurResponse.data.link);
                createEvent(event);

            }

            @Override
            public void onCompleted() {
                //getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_success));
            }

            @Override
            public void onError(Throwable e) {
                getViewLayer().showLoading(false);
                if (e instanceof UnknownHostException) {
                    getViewLayer().showBasicError(getViewLayer().getResources().getString(R.string.network_not_accessible));
                } else {
                    getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_failed));
                }
            }
        };

        rx.Observable<ImgurResponse> eventStream = eventManager.uploadImage(imageFile);
        eventStream.subscribe(eventObserver);
    }

    public void createEvent(Event event) {
        Observer<ErrorResponseComplete> eventObserver = new Observer<ErrorResponseComplete>() {
            @Override
            public void onNext(ErrorResponseComplete events) {
                if (events.errors != null || events.errors.size() > 0)
                    getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_failed));
                else
                    getViewLayer().showLoading(false);
            }

            @Override
            public void onCompleted() {
                getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_success));
            }

            @Override
            public void onError(Throwable e) {
                getViewLayer().showLoading(false);
                if (e instanceof UnknownHostException) {
                    getViewLayer().showBasicError(getViewLayer().getResources().getString(R.string.network_not_accessible));
                } else {
                    getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_failed));
                }
            }
        };

        rx.Observable<ErrorResponseComplete> eventStream = eventManager.createEvent(event);
        eventStream.subscribe(eventObserver);
    }
}
