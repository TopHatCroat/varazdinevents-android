package hr.foi.varazdinevents;

import hr.foi.varazdinevents.api.responses.ImgurResponse;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Antonio Martinović on 02.01.17.
 */

public interface ImgurService {
    @Headers("Authorization: Client-ID d66aa7705e07d67")
    @POST("image")
    @Multipart
    Observable<ImgurResponse> uploadImage(@Part("image") RequestBody file); //, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("type") RequestBody type
}
