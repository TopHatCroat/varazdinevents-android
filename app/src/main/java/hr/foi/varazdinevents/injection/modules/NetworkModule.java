package hr.foi.varazdinevents.injection.modules;

import dagger.Module;


@Module
public class NetworkModule {


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