package hr.foi.varazdinevents.api;

import com.google.firebase.iid.FirebaseInstanceId;
import com.orm.query.Condition;
import com.orm.query.Select;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.api.responses.EventResponse;
import hr.foi.varazdinevents.api.responses.ImgurResponse;
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

    /**
     * Uploads an image to the Imgur service and returns image data which includes a link to it
     * @param image loaded File containing the image
     * @return ImgurResponse with status code and response data
     */
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

    /**
     * Loads data from memory
     * @return all events loaded in memory
     */
    private Observable<List<Event>> fromMemory(){
        return Observable.just(events).doOnNext(new Action1<List<Event>>() {
            @Override
            public void call(List<Event> events) {
                Timber.w("Loading from memory...");
            }
        });
    }

    /**
     * Loads event data from database that has the DATE_TO attribute set to lesser than now
     * @return events from database
     */
    private Observable<List<Event>> fromDatabase() {
        return Observable.just(
                Select.from(Event.class)
                        .where(Condition.prop("DATE")
                        .gt(System.currentTimeMillis()))
                        .list()
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

    /**
     * Loads all new events from the API and stores the new ones to the database and the memory
     * @return all recently changed or new events
     */
    private Observable<List<Event>> fromNetwork() {
        String lastUpdateValue = String.valueOf(SharedPrefs.read(LAST_UPDATE_TIME_KEY, 0));

        int cityId = SharedPrefs.read("city", 1);

//        return restService.getEvents(lastUpdateValue)

        return restService.getEventsByCity(cityId)
                .map(new Func1<EventResponse[], List<Event>>() {
                    @Override
                    public List<Event> call(EventResponse[] eventResponses) {
                        List<Event> events = new LinkedList<>();
                        for(EventResponse eventResponse : eventResponses){
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
                            if(eventResponse.hostApiId != null) event.setHostApiId(eventResponse.hostApiId);
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
                       toDatabase(events);
                   }
               })
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Stores events into memory
     * @param events to save
     */
    private void toMemory(List<Event> events) {
        Timber.w("Saving to memory...");

        Map<Integer, Event> eventsMap = new HashMap<>();
        for (Event event : this.events) {
            eventsMap.put(event.apiId, event);
        }
        for (Event event : events) {
            if(!eventsMap.containsKey(event.apiId) && event.getDate() > System.currentTimeMillis())
                eventsMap.put(event.apiId, event);
        }

        this.events = new ArrayList<Event>(eventsMap.values());
    }

    /**
     * Stores events into the database
     * @param events to save
     */
    private void toDatabase(List<Event> events) {
        Timber.w("Saving to database...");
        Event tmp;
        for (Event event : events) {
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

    /**
     * Toggles favorite event on/off
     * @param event
     * @return Is the event favorited
     */
    public static boolean toggleFavorite(Event event){
        Event tmp = Select.from(Event.class).where(Condition.prop("API_ID").eq(event.getApiId())).first();
        tmp.isFavorite = !tmp.isFavorite;
        tmp.save();
        return tmp.isFavorite;
    }

    public boolean setFavorite(Event event, boolean value){
        Event tmp = Select.from(Event.class).where(Condition.prop("API_ID").eq(event.getApiId())).first();
        tmp.isFavorite = value;
        tmp.save();
        toMemory(Arrays.asList(event));
        return tmp.isFavorite;
    }

    public Observable<JSONObject> setEventFavourite(final Event event, final boolean favouriteState) {
        Observable<JSONObject> favouriteObservable;
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        if(favouriteState) {
            favouriteObservable = restService.favouriteEvent(event.getApiId(), firebaseToken);
        } else {
            favouriteObservable = restService.unfavouriteEvent(event.getApiId(), firebaseToken);
        }

        return favouriteObservable
            .doOnNext(new Action1<JSONObject>() {
                @Override
                public void call(JSONObject json) {
                    setFavorite(event, favouriteState);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
