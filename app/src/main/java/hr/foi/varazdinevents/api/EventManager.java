package hr.foi.varazdinevents.api;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Antonio Martinović on 30.10.16.
 */
public class EventManager {

    private User user;
    private RestService restService;

    private List<Event> events;

    public EventManager(User user, RestService restService) {
        this.restService = restService;
        this.user = user;
        events = new ArrayList<>();
    }

    public Observable<List<Event>> getEvents() {
        return Observable.concat(
                fromMemory(), fromDatabase(), fromNetwork())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private Observable<List<Event>> fromMemory(){
        return Observable.just(events).doOnNext(new Action1<List<Event>>() {
            @Override
            public void call(List<Event> events) {
                Timber.w("Loading from memory...");
            }
        });
    }


    private Observable<List<Event>> fromDatabase() {
        return Observable.just(
                Select.from(Event.class).where(
                        Condition.prop("DATE_TO").lt(System.currentTimeMillis()/1000)).list())
                .doOnNext(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        Timber.w("Loading from database...");
                        toMemory(events);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Event>> fromNetwork() {
       return restService.getEvents()
                .map(new Func1<EventResponseComplete, List<Event>>() {
                    @Override
                    public List<Event> call(EventResponseComplete eventResponses) {
                        List<Event> events = new LinkedList<>();
                        for(EventResponse eventResponse : eventResponses.items){
                            Event event = new Event();
                            event.setApiId(eventResponse.id);
                            event.setTitle(eventResponse.title);
                            event.setText(eventResponse.text);
                            event.setDate(eventResponse.date);
                            event.setDateTo(eventResponse.dateTo);
                            event.setHost(eventResponse.host);
                            event.setOfficialLink(eventResponse.officialLink);
                            event.setImage(eventResponse.image);
                            event.setFacebook(eventResponse.facebook);
                            event.setOffers(eventResponse.offers);
                            event.setCategory(eventResponse.category);
                            events.add(event);
                        }
                        return events;
                    }
                })
               .doOnNext(new Action1<List<Event>>() {
                   @Override
                   public void call(List<Event> events) {
                       Timber.w("Loading from REST...");
                       toDatabase(events);
                       toMemory(events);
                   }
               })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void toMemory(List<Event> events) {
        Timber.w("Saving to memory...");

        Map<Integer, Event> eventsMap = new HashMap<>();
        for (Event event : this.events) {
            eventsMap.put(event.apiId, event);
        }
        for (Event event : events) {
            eventsMap.put(event.apiId, event);
        }

        this.events = new ArrayList<Event>(eventsMap.values());

    }

    private void toDatabase(List<Event> events) {
        Timber.w("Saving to database...");
        Event.saveInTx(events);

    }


}
