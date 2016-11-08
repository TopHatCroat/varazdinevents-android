package hr.foi.varazdinevents.injection;

import android.app.Activity;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;

/**
 * Created by Antonio Martinović on 15.10.16.
 */

public final class ActivityComponentFactory {
    private ActivityComponentFactory(){

    }

    public static MainActivityComponent create(Activity activity) {
        return MainApplication.get(activity).getComponent()
                .plus(new MainActivityModule(activity));
    }
}
