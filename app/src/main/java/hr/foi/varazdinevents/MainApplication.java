package hr.foi.varazdinevents;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import com.facebook.FacebookSdk;
import com.orm.SugarContext;

import java.util.Locale;

import timber.log.Timber;

import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ApplicationComponent;
import hr.foi.varazdinevents.injection.DaggerApplicationComponent;
import hr.foi.varazdinevents.injection.UserComponent;
import hr.foi.varazdinevents.injection.modules.ApplicationModule;
import hr.foi.varazdinevents.injection.modules.UserModule;
import hr.foi.varazdinevents.models.User;

import static hr.foi.varazdinevents.util.Constants.PREF_LANG_KEY;

/**
 * Created by Antonio Martinović on 15.10.16.
 */
public class MainApplication extends MultiDexApplication {
    ApplicationComponent applicationComponent;
    UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        FacebookSdk.sdkInitialize(this);

        getApplicationComponent();
        createUserComponent();
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);

        Locale myLocale = new Locale(prefs.getString(PREF_LANG_KEY, "en"));
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
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

    public UserComponent getUserComponent() {
        return userComponent;
    }


    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public UserComponent createUserComponent() {
        if (userComponent == null) createUserComponent(UserManager.getLoggedInUser());
        return userComponent;
    }

    public void createUserComponent(User user) {
        this.userComponent = applicationComponent.plus(new UserModule(user));
    }

    public void setUserComponent(UserComponent userComponent) {
        this.userComponent = userComponent;
    }

}