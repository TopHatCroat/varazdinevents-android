package hr.foi.varazdinevents.api;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.api.responses.ErrorResponseComplete;
import hr.foi.varazdinevents.api.responses.EventResponse;
import hr.foi.varazdinevents.api.responses.EventResponseComplete;
import hr.foi.varazdinevents.api.responses.ImgurResponse;
import hr.foi.varazdinevents.api.responses.NewEventPojo;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.util.SharedPrefs;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static hr.foi.varazdinevents.util.Constants.LAST_UPDATE_TIME_KEY;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

/**
* Contains all methods for working with events such as getting all events and creating new ones
 */
public class EventManager {
    private final SharedPrefs sharedPrefs;
    private final User user;
    private final RestService restService;
    private final ImgurService imgurService;

    private Event newEvent;

    private List<Event> events;

    /**
     * @param user current user logged in
     * @param restService Retrofit API calls interface
     */
    public EventManager(User user, RestService restService, ImgurService imgurService,
                        SharedPrefs sharedPrefs) {
        this.restService = restService;
        this.user = user;
        this.imgurService = imgurService;
        this.sharedPrefs = sharedPrefs;
        events = new ArrayList<>();
    }

    /**
     * Gets all events from API
     * @return list of events
     */
    public Observable<List<Event>> getEvents() {
        return Observable.concat(fromMemory(), fromDatabase(), fromNetwork())
                .cache()
                .map(new Func1<List<Event>, List<Event>>() {
                    @Override
                    public List<Event> call(List<Event> events) {
                        toMemory(events);
                        return EventManager.this.events;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<ErrorResponseComplete> createEvent(Event event) {
        NewEventPojo createEvent = new NewEventPojo();
        createEvent.title = event.getTitle();
        createEvent.text = event.getText();
        createEvent.date = event.getDate() / 1000;
        createEvent.time = new SimpleDateFormat("HH:mm:ss").format(event.date);
        createEvent.dateTo = event.getDateTo() / 1000;
        createEvent.timeTo = new SimpleDateFormat("HH:mm:ss").format(event.dateTo);
        createEvent.host = event.getHost();
        createEvent.officialLink = event.getOfficialLink();
        createEvent.image = event.getImage();
        createEvent.facebook = event.getFacebook();
        createEvent.offers = event.getOffers();
        createEvent.category = event.getCategory();

        return restService.createEvent(this.user.getToken(), createEvent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ImgurResponse> uploadImage(File image) {
        String type;
        if (image.getAbsolutePath().endsWith("png")) {
            type = "image/png";
        } else if (image.getAbsolutePath().endsWith("gif")) {
            type = "image/gif";
        } else {
            type = "image/jpeg";
        }

        RequestBody uploadFile = RequestBody.create(MediaType.parse(type), image);

        return imgurService.uploadImage(uploadFile)
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
                //Event.listAll(Event.class)
                Select.from(Event.class).where(Condition.prop("DATE_TO").lt(System.currentTimeMillis()/1000)).list()
                )
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
        String lastUpdateValue = String.valueOf(SharedPrefs.read(LAST_UPDATE_TIME_KEY, 0));

        return restService.getEvents(lastUpdateValue)
                .map(new Func1<EventResponseComplete, List<Event>>() {
                    @Override
                    public List<Event> call(EventResponseComplete eventResponses) {
                        List<Event> events = new LinkedList<>();
                        for(EventResponse eventResponse : eventResponses.items){
                            Event event = new Event();
                            event.setApiId(eventResponse.id);
                            event.setTitle(eventResponse.title);
                            event.setText(eventResponse.text);
                            if(eventResponse.date != null) event.setDate(eventResponse.date * 1000L);
                            if(eventResponse.dateTo != null) event.setDateTo(eventResponse.dateTo * 1000L);
                            event.setHost(eventResponse.host);
                            event.setOfficialLink(eventResponse.officialLink);
                            event.setImage(eventResponse.image);
                            event.setFacebook(eventResponse.facebook);
                            event.setOffers(eventResponse.offers);
                            event.setCategory(eventResponse.category);
                            event.setDateUpdated(eventResponse.lastUpdate);
                            event.setHostApiId(eventResponse.hostApiId);
                            events.add(event);

                            int lastUpdate = SharedPrefs.read(LAST_UPDATE_TIME_KEY, 0);
                            if(lastUpdate < eventResponse.lastUpdate)
                                SharedPrefs.write(LAST_UPDATE_TIME_KEY, eventResponse.lastUpdate);
                        }
                        return events;
                    }
                })
               .doOnNext(new Action1<List<Event>>() {
                   @Override
                   public void call(List<Event> events) {
                       Timber.w("Loading from REST...");
                       toMemory(events);
                       toDatabase(events);
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
            if(!eventsMap.containsKey(event.apiId))
                eventsMap.put(event.apiId, event);
        }

        this.events = new ArrayList<Event>(eventsMap.values());
    }

    private void toDatabase(List<Event> events) {
        Timber.w("Saving to database...");
        Event tmp;
        for (Event event : this.events) {
            tmp = Select.from(Event.class)
                    .where(Condition.prop("API_ID").eq(event.apiId))
                    .first();
            if(tmp == null) {
                Event.save(event);
            } else if(event.getDateUpdated() > tmp.getDateUpdated()) {
                tmp = event;
                tmp.save();
            }
        }
    }

    public Event getNewEvent() {
        if(this.newEvent == null) {
            this.newEvent = new Event();
        }
        return newEvent;
    }

    public void setNewEvent(Event newEvent) {
        this.newEvent = newEvent;
    }
    public static boolean toggleFavorite(Event event){
        Event tmp = Select.from(Event.class).where(Condition.prop("API_ID").eq(event.getApiId())).first();
        tmp.isFavorite = !tmp.isFavorite;
        tmp.save();
        return tmp.isFavorite;
    }

}
