package hr.foi.varazdinevents.places.events;

import com.google.common.collect.ImmutableList;

import javax.inject.Inject;

import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import rx.Observer;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainPresenter extends BasePresenter<MainView> {
    @Inject
    EventManager eventManager;


    public void loadEvents(){
        checkViewAttached();

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
}
