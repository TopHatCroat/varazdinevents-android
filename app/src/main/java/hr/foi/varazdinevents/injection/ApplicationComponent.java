package hr.foi.varazdinevents.injection;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.injection.modules.ApplicationModule;
import hr.foi.varazdinevents.injection.modules.NetworkModule;

/**
 * Created by Antonio Martinović on 15.10.16.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
})
public interface ApplicationComponent {
    @NonNull
    MainActivityComponent plus(@NonNull MainActivityModule mainActivityModule);

    @NonNull
    EventDetailsActivityComponent plus(@NonNull EventDetailsActivityModule eventDetailsActivityModule);

//    void inject(@NonNull MainApplication mainApplication);
}
