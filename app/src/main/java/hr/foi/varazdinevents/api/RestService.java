package hr.foi.varazdinevents.api;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

public interface RestService {
    @GET("events")
    Observable<EventResponseComplete> getEvents();

    @POST("user/login")
    @FormUrlEncoded
    Observable<UserResponse> loginUser(@Field("username") String username, @Field("password") String password);

    @GET("user/login")
    Observable<UserResponse> logutUser(@Query("token") String token);


}