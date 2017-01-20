package hr.foi.varazdinevents.places.events;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.elements.list.ListListener;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;

import rx.Observer;
import android.view.View;
import java.util.List;
/**
 * Created by Antonio Martinović on 12.10.16.
 */

public class MainPresenter extends BasePresenter<MainActivity> implements ListListener<Event>{
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
                getViewLayer().showLoading(false);
            }

            @Override
            public void onCompleted() {

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

    public void onItemClick(Event item) {
        EventDetailsActivity.startWithEvent(item, getViewLayer());
    }

    @Override
    public void onItemClick(Event item, View view) {
        getViewLayer().animateOut();
        EventDetailsActivity.startWithEventAnimated(item, getViewLayer(), view);
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
