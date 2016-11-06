package hr.foi.varazdinevents;

import android.app.Application;
import android.content.Context;

import hr.foi.varazdinevents.injection.ApplicationComponent;
import hr.foi.varazdinevents.injection.DaggerApplicationComponent;
import hr.foi.varazdinevents.injection.modules.ApplicationModule;


/**
 * Created by Antonio Martinović on 15.10.16.
 */
public class MainApplication extends Application {
    ApplicationComponent applicationComponent;

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

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
//            applicationComponent = DaggerApplicationComponent.create();

            applicationComponent = DaggerApplicationComponent.builder()
                                .applicationModule(new ApplicationModule(this))
                                .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}