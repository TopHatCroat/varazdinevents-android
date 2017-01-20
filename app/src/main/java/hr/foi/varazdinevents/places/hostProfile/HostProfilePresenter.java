package hr.foi.varazdinevents.places.hostProfile;

import android.view.View;

import java.util.List;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import hr.foi.varazdinevents.ui.elements.list.ListListener;
import rx.Observer;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

public class HostProfilePresenter extends BasePresenter<HostProfileActivity> implements ListListener<Event>{
    private EventManager eventManager;
    private UserManager userManager;


    public HostProfilePresenter(UserManager userManager, EventManager eventManager){
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    public void itemClicked(Object item) {
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
    public void onItemClick(Event item) {
        EventDetailsActivity.startWithEvent(item, getViewLayer());
    }

    @Override
    public void onItemClick(Event item, View view) {
        EventDetailsActivity.startWithEvent(item, getViewLayer());
    }
}
