package hr.foi.varazdinevents.api;

import com.google.common.collect.ImmutableList;

import java.util.List;

import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Antonio Martinović on 30.10.16.
 */
public class EventManager {

    private User user;

    private RestService restService;

    public EventManager(User user, RestService restService) {
        this.restService = restService;
        this.user = user;
    }

    public Observable<ImmutableList<Event>> getEvents() {
        return restService.getEvents()
                .map(new Func1<EventResponseComplete, ImmutableList<Event>>() {
                    @Override
                    public ImmutableList<Event> call(EventResponseComplete eventResponses) {
                        final ImmutableList.Builder<Event> eventBuilder = ImmutableList.builder();
                        for(EventResponse eventResponse : eventResponses.items){
                            Event event = new Event();
                            event.setId(eventResponse.id);
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
                            eventBuilder.add(event);
                        }
                        return eventBuilder.build();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
