package hr.foi.varazdinevents.injection;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.injection.modules.ApplicationModule;
import hr.foi.varazdinevents.injection.modules.NetworkModule;
import hr.foi.varazdinevents.injection.modules.UserModule;

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
    UserComponent plus(UserModule userModule);


//    void inject(@NonNull MainApplication mainApplication);
}
