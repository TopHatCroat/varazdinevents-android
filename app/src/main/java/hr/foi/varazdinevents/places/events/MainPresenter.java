package hr.foi.varazdinevents.places.events;

import com.google.common.collect.ImmutableList;

import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import rx.Observer;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainPresenter extends BasePresenter<MainView> {
    private EventManager eventManager;

    public MainPresenter(EventManager eventManager) {
        this.eventManager = eventManager;
    }


    public void loadEvents(){
        checkViewAttached();
        getViewLayer().showLoading(true);

        eventManager.getEvents().subscribe(new Observer<ImmutableList<Event>>() {
            @Override
            public void onNext(ImmutableList<Event> events) {
                getViewLayer().showEvents(events);
            }

            @Override
            public void onCompleted() {
                getViewLayer().showLoading(false);
            }

            @Override
            public void onError(Throwable e) {
                getViewLayer().showLoading(false);
                getViewLayer().showBasicError("ne radi");
            }
        });
    }

    @Override
    public void itemClicked(Object item) {
        getViewLayer().onItemClicked(item);
    }
}
