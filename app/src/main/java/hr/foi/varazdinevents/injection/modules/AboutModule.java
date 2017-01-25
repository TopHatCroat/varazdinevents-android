package hr.foi.varazdinevents.injection.modules;


import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.about.AboutActivity;

/**
 * Created by Antonio Martinović on 22.01.17.
 */

@Module
public class AboutModule  {
    private AboutActivity activity;

    public AboutModule(AboutActivity activity) {
        this.activity = activity;
    }

    @Provides
    public AboutActivity provideActivity() {
        return this.activity;
    }

    @Provides
    @ActivityScope
    public Context provideContext() {
        return this.activity;
    }
}
