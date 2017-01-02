package hr.foi.varazdinevents.injection.modules;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.api.RestService;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.UserScope;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.util.SharedPrefs;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;


@Module
public class NetworkModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS).readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(MainApplication mainApplication, OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient).baseUrl("http://varazdinevents.cf/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    public RestService provideRestService(Retrofit restAdapter) {
        return restAdapter.create(RestService.class);
    }

    @Provides
    @Singleton
    public UserManager provideUserManager(RestService restService, SharedPrefs sharedPrefs){
        return new UserManager(restService, sharedPrefs);
    }

//    @Provides
//    @NonNull
//    @Singleton
//    public OkHttpClient providClient(@ClientCache File cacheDir, CacheInterceptor interceptor,
//                                     @NonNull AppPreferences appPreferences) {
//        final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
//        Cache cache = new Cache(cacheDir, 20 * 1024 * 1024);
//        okHttpBuilder.cache(cache);
//        okHttpBuilder.interceptors().add(interceptor); //needed for force network
//        okHttpBuilder.interceptors().add(getAuthInterceptor(appPreferences));
//        okHttpBuilder.networkInterceptors().add(interceptor); //needed for offline mode
//        return okHttpBuilder.build();
//    }
//
//    @NonNull
//    private Interceptor getAuthInterceptor(@NonNull AppPreferences appPreferences) {
//        return chain -> {
//            Request.Builder newRequest = chain
//                    .request()
//                    .newBuilder();
//            if (appPreferences.getPreference(AppPreferences.AUTH_TOKEN, null) != null)
//            {
//                newRequest.addHeader("X-USER-TOKEN",
//                        appPreferences.getPreference(AppPreferences.AUTH_TOKEN, null)).build();
//            }
//            return chain.proceed(newRequest.build());
//        };
//    }
//
//    @Singleton
//    @Provides
//    @ClientCache
//    File provideCacheFile(Application context)     {
//        return new File(context.getCacheDir(), "cache_file");
//    }


}
