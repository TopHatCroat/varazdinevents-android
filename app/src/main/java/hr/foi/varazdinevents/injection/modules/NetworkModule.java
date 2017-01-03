package hr.foi.varazdinevents.injection.modules;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.ImgurService;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.api.RestService;
import hr.foi.varazdinevents.api.UserManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;


@Module
public class NetworkModule {
    @Provides
    @Singleton
    @Named("vzclient")
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
    @Named("vzadapter")
    public Retrofit provideRestAdapter(MainApplication mainApplication, @Named("vzclient") OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient).baseUrl("http://varazdinevents.cf/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("vzservice")
    public RestService provideRestService(@Named("vzadapter") Retrofit restAdapter) {
        return restAdapter.create(RestService.class);
    }

    @Provides
    @Singleton
    @Named("imgurclient")
    public OkHttpClient provideImgurOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);

//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder().addHeader("Client-ID", "d66aa7705e07d67");
//                Request request  = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS).readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    @Named("imguradapter")
    public Retrofit provideImagurRestAdapter(MainApplication mainApplication, @Named("imgurclient") OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient).baseUrl("https://api.imgur.com/3/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    @Named("imgurservice")
    public ImgurService provideImagurRestService(@Named("imguradapter") Retrofit restAdapter) {
        return restAdapter.create(ImgurService.class);
    }

    @Provides
    @Singleton
    public UserManager provideUserManager(@Named("vzservice") RestService restService){
        return new UserManager(restService);
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
