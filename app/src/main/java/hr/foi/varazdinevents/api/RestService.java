package hr.foi.varazdinevents.api;

import java.util.List;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

public interface RestService {
    @GET("events")
    Observable<EventResponseComplete> getEvents();
}