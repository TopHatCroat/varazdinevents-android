package hr.foi.varazdinevents.api;

import org.json.JSONObject;

import hr.foi.varazdinevents.api.responses.CityResponseComplete;
import hr.foi.varazdinevents.api.responses.EventResponse;
import hr.foi.varazdinevents.api.responses.FestivalResponseComplete;
import hr.foi.varazdinevents.api.responses.UserResponse;
import hr.foi.varazdinevents.api.responses.UserResponseComplete;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Antonio Martinović on 30.10.16.
 */

/**
 * Retrofit interface for defining API calls
 */
public interface RestService {
    /**
     * API call for getting events
     *
     * @return Response containing a list of events and it's meta data
     */
    @GET("event/query")
    // /query/{timestamp}"
    Observable<EventResponse[]> getEvents(@Query("timestamp") String timestamp);

    @GET("events/city/{cityId}")
        // /query/{timestamp}"
    Observable<EventResponse[]> getEventsByCity(@Path("cityId") Integer cityId);

    /**
     * API call for getting event hosts
     *
     * @param timestamp
     * @return Response containing a list of hosts and it's meta data
     */
    @GET("hosts")
    Observable<UserResponseComplete> getUsers(@Query("timestamp") String timestamp);

    /**
     * API call for user log in
     *
     * @param username user's nickname
     * @param password user's passoword
     * @return User response with token
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<UserResponse> loginUser(@Field("username") String username, @Field("password") String password);

    /**
     * API call for user log out
     *
     * @param token token of the user requesting log out
     * @return User response with null token
     */
    @GET("user/login")
    Observable<UserResponse> logoutUser(@Query("token") String token);

    /**
     * API call for firebase implementation
     *
     * @param token
     * @return Firebase response with token
     */
    @GET("firebase/add/{token}")
    Observable<JSONObject> sendFCMToken(@Path("token") String token);

    @GET("firebase/favorite/{eventId}")
    Observable<JSONObject> favouriteEvent(@Path("eventId") Integer apiId, @Query("token") String firebaseToken);

    @GET("firebase/un-favorite/{eventId}")
    Observable<JSONObject> unfavouriteEvent(@Path("eventId") Integer apiId, @Query("token") String firebaseToken);

    @GET("cities")
    Observable<CityResponseComplete> getCities();

    @GET("festival")
    Observable<FestivalResponseComplete> getFestivals();
}