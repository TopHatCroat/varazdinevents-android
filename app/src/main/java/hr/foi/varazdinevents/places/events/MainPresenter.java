package hr.foi.varazdinevents.places.events;

import java.util.List;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.ui.elements.list.ListListener;
import rx.Observer;

import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainPresenter extends BasePresenter<MainActivity> implements ListListener{
    private EventManager eventManager;

    public MainPresenter(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void loadEvents(){
        checkViewAttached();
        getViewLayer().showLoading(true);

        Observer<List<Event>> eventObserver = new Observer<List<Event>>() {
            @Override
            public void onNext(List<Event> events) {
                getViewLayer().showEvents(events);
            }

            @Override
            public void onCompleted() {
                getViewLayer().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getViewLayer().showLoading(false);
                    getViewLayer().showBasicError(getViewLayer().getString(R.string.network_not_accessible));
                }
            }
        };

        rx.Observable<List<Event>> eventStream = eventManager.getEvents();
        eventStream.subscribe(eventObserver);
    }

    @Override
    public void onItemClick(Object item) {
        getViewLayer().onItemClicked(item);

    }
}
