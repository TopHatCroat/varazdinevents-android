package hr.foi.varazdinevents.api;

import java.util.List;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

public interface RestService {
    @GET("content/index/jfeed?appname=testapp&appkey=60a34f8a0f85197282cee38a889c549a")
    Observable<List<EventResponse>> getEvents();
}