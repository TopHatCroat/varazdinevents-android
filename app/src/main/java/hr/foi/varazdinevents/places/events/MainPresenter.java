package hr.foi.varazdinevents.places.events;

import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.CityManager;
import hr.foi.varazdinevents.api.FestivalManager;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.City;
import hr.foi.varazdinevents.models.Festival;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;
import hr.foi.varazdinevents.ui.elements.list.ListListener;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;

import rx.Observer;
import android.view.View;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;
/**
 * Created by Antonio Martinović on 12.10.16.
 */

/**
 * Presenter for Main activity, contains additional methods for displaying events
 */
public class MainPresenter extends BasePresenter<MainActivity> implements ListListener<Event> {
    private final CityManager cityManager;
    private final FestivalManager festivalManager;
    private EventManager eventManager;
    private UserManager userManager;

    public MainPresenter(EventManager eventManager, UserManager userManager, CityManager cityManager, FestivalManager festivalManager) {
        this.eventManager = eventManager;
        this.userManager = userManager;
        this.cityManager = cityManager;
        this.festivalManager = festivalManager;
    }

    public void loadCities() {
        Observer<List<City>> eventObserver = new Observer<List<City>>() {
            @Override
            public void onNext(List<City> cities) {
                getViewLayer().showLocationPicker(cities);
            }

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getViewLayer().showLoading(false);
                    getViewLayer().showBasicError(getViewLayer().getString(R.string.network_not_accessible));
                }
            }
        };

        rx.Observable<List<City>> cityStream = cityManager.getCities();
        cityStream.subscribe(eventObserver);
    }

    /**
     * Gets and shows list of upcoming events
     */
    public void loadEvents(){
        checkViewAttached();
        getViewLayer().showLoading(true);

        Observer<List<Event>> eventObserver = new Observer<List<Event>>() {
            @Override
            public void onNext(List<Event> events) {
                loadFestivals(events);
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

    private void loadFestivals(final List<Event> events) {

        Observer<List<Festival>> festivalObserver = new Observer<List<Festival>>() {
            @Override
            public void onNext(List<Festival> festivals) {
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

        rx.Observable<List<Festival>> eventStream = festivalManager.getFestivals();
        eventStream.subscribe(festivalObserver);
    }

    /**
     * Default method, starts "Event Details" activity if an event is clicked
     * @param item object of item clicked
     */
    public void onItemClick(Event item) {
        EventDetailsActivity.startWithEvent(item, getViewLayer());
    }

    /**
     * Starts "Event Details" activity with animated effects if an event is clicked
     * @param item object of item clicked
     * @param view animation target
     */
    @Override
    public void onItemClick(Event item, View view) {
        getViewLayer().animateOut();
        EventDetailsActivity.startWithEventAnimated(item, getViewLayer(), view);
    }

    /**
     * Calls method which loads list of users
     */
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

    public List<Event> refreshEvents() {
        return Select.from(Event.class)
                .where(Condition.prop("DATE")
                .gt(System.currentTimeMillis()))
                .list();
    }
}
