package hr.foi.varazdinevents.places.newEvent;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.responses.ErrorResponseComplete;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import rx.Observer;

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
        final Event event = (Event) item;


        checkViewAttached();
        getViewLayer().showLoading(true);

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
                getViewLayer().showBasicError(getViewLayer().getString(R.string.event_create_failed));
                e.printStackTrace();
            }
        };

        rx.Observable<ErrorResponseComplete> eventStream = eventManager.createEvent(event);
        eventStream.subscribe(eventObserver);
    }
}
