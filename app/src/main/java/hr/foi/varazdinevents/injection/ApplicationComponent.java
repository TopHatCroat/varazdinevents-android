package hr.foi.varazdinevents.injection;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.injection.modules.ActivityModule;
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
    ActivityComponent plus(@NonNull ActivityModule activityModule);

//    void inject(@NonNull MainApplication mainApplication);
}
