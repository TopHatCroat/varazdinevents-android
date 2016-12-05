package hr.foi.varazdinevents.api;

import hr.foi.varazdinevents.api.responses.ErrorResponseComplete;
import hr.foi.varazdinevents.api.responses.EventResponse;
import hr.foi.varazdinevents.api.responses.EventResponseComplete;
import hr.foi.varazdinevents.api.responses.NewEventPojo;
import hr.foi.varazdinevents.api.responses.UserResponse;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

/**
 * Retrofit interface for defining API calls
 */
public interface RestService {
    /**
     * API call for getting events
     * @return Response containing a list of events and it's meta data
     */
    @GET("events") // /query/{timestamp}"
    Observable<EventResponseComplete> getEvents();

    /**
     * API call for user log in
     * @param username user's nickname
     * @param password user's passoword
     * @return User response with token
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<UserResponse> loginUser(@Field("username") String username, @Field("password") String password);

    /**
     * API call for user log out
     * @param token token of the user requesting log out
     * @return User response with null token
     */
    @GET("user/login")
    Observable<UserResponse> logutUser(@Query("token") String token);

    @POST("events")
    Observable<ErrorResponseComplete> createEvent(@Query("token") String token, @Body NewEventPojo json);
}