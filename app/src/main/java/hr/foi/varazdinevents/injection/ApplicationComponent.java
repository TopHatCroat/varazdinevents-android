package hr.foi.varazdinevents.injection;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Component;
import hr.foi.varazdinevents.injection.modules.LoginActivityModule;
import hr.foi.varazdinevents.injection.modules.ApplicationModule;
import hr.foi.varazdinevents.injection.modules.NetworkModule;
import hr.foi.varazdinevents.injection.modules.RegisterActivityModule;
import hr.foi.varazdinevents.injection.modules.UserModule;

/**
 * Base component class specifying the exact subcomponent that will have
 * access objects provided by {@link ApplicationModule} and {@link NetworkModule};
 * Created by Antonio Martinović on 15.10.16.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
})
public interface ApplicationComponent {

    @NonNull
    LoginActivityComponent plus(LoginActivityModule loginActivityModule);

    @NonNull
    RegisterActivityComponent plus(RegisterActivityModule registerActivityComponent);

    @NonNull
    UserComponent plus(UserModule userModule);


//    void inject(@NonNull MainApplication mainApplication);
}
