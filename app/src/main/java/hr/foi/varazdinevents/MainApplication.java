package hr.foi.varazdinevents;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ApplicationComponent;
import hr.foi.varazdinevents.injection.DaggerApplicationComponent;
import hr.foi.varazdinevents.injection.UserComponent;
import hr.foi.varazdinevents.injection.modules.ApplicationModule;
import hr.foi.varazdinevents.injection.modules.UserModule;
import hr.foi.varazdinevents.models.User;


/**
 * Created by Antonio Martinović on 15.10.16.
 */
public class MainApplication extends MultiDexApplication {
    ApplicationComponent applicationComponent;
    UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        }
    }

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
//            applicationComponent = DaggerApplicationComponent.create();

            applicationComponent = DaggerApplicationComponent.builder()
                                .applicationModule(new ApplicationModule(this))
                                .build();
        }
        return applicationComponent;
    }

    public UserComponent createUserComponent(User user) {
        userComponent = applicationComponent.plus(new UserModule(user));
        return userComponent;
    }


    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public UserComponent getUserComponent() {
        if (userComponent == null) createUserComponent(UserManager.getStubUser("test"));
        return userComponent;
    }

    public void setUserComponent(UserComponent userComponent) {
        this.userComponent = userComponent;
    }

}