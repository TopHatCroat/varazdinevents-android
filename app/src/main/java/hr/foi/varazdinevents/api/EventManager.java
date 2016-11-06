package hr.foi.varazdinevents.api;

import com.google.common.collect.ImmutableList;

import java.util.List;

import hr.foi.varazdinevents.models.Event;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Antonio Martinović on 30.10.16.
 */
public class EventManager {

    private RestService restService;

    public EventManager(RestService restService) {
        this.restService = restService;
    }

    public Observable<ImmutableList<Event>> getEvents() {
        return restService.getEvents()
                .map(new Func1<List<EventResponse>, ImmutableList<Event>>() {
                    @Override
                    public ImmutableList<Event> call(List<EventResponse> eventResponses) {
                        final ImmutableList.Builder<Event> eventBuilder = ImmutableList.builder();
                        for(EventResponse eventResponse : eventResponses){
                            Event event = new Event();
                            event.setId(eventResponse.id);
                            event.setVisible(eventResponse.visible);
                            event.setDateAdded(eventResponse.dateAdded);
                            event.setTitle(eventResponse.title);
                            event.setText(eventResponse.text);
                            event.setDate(eventResponse.date);
                            event.setTime(eventResponse.time);
                            event.setDateTo(eventResponse.dateTo);
                            event.setTimeTo(eventResponse.timeTo);
                            event.setHost(eventResponse.host);
                            event.setOfficialLink(eventResponse.officialLink);
                            event.setImage(eventResponse.image);
                            event.setFacebook(eventResponse.facebook);
                            event.setOffers(eventResponse.offers);
                            event.setFbId(eventResponse.fbId);
                            event.setAuthor(eventResponse.author);
                            eventBuilder.add(event);
                        }
                        return eventBuilder.build();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
