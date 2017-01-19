package hr.foi.varazdinevents.places.events;

import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
import rx.Observable;
import rx.Observer;

import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainPresenter extends BasePresenter<MainActivity> {
    private EventManager eventManager;
    private UserManager userManager;

    public MainPresenter(EventManager eventManager, UserManager userManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
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
                getViewLayer().showLoading(false);
                getViewLayer().showBasicError(getViewLayer().getString(R.string.network_not_accessible));
                e.printStackTrace();
            }
        };

        rx.Observable<List<Event>> eventStream = eventManager.getEvents();
        eventStream.subscribe(eventObserver);
    }

    @Override
    public void itemClicked(Object item) {
        getViewLayer().onItemClicked(item);
    }

    public void loadUsers() {

        Observer<List<User>> hostObserver = new Observer<List<User>>() {
            @Override
            public void onNext(List<User> users) {}

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                getViewLayer().showBasicError(getViewLayer().getString(R.string.network_not_accessible));
            }
        };

        rx.Observable<List<User>> hostStream = userManager.getUsers();
        hostStream.subscribe(hostObserver);
    }
}
