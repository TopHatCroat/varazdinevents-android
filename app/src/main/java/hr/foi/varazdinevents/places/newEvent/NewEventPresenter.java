package hr.foi.varazdinevents.places.newEvent;

import android.net.Uri;
import java.io.File;
import java.net.UnknownHostException;
import rx.Observer;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.responses.ErrorResponseComplete;
import hr.foi.varazdinevents.api.responses.ImgurResponse;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import hr.foi.varazdinevents.util.FileUtils;
/**
 * Created by Antonio Martinović on 03.12.16.
 */
public class NewEventPresenter extends BasePresenter<NewEventActivity> {

    private final EventManager eventManager;

    public NewEventPresenter(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Uploads the image to Imgur service and when completed calls the
     * {@link NewEventPresenter#createEvent(Event)} to try creating the event
     * with the image link attribute updated with the link of the image
     * @param event event to create
     * @param image image associated with event
     */
    void uploadImage(final Event event, Uri image) {
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
                getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_success));
                getViewLayer().onBackPressed();
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

    /**
     * Makes a new request to the API with data for event creation, when completed
     * it shows the status messages on the view layer and exists the activity
     * if successful
     * @param event event to create
     */
    private void createEvent(Event event) {
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
