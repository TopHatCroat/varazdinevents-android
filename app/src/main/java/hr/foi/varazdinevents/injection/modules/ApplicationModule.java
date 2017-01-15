package hr.foi.varazdinevents.injection.modules;

import android.transition.Fade;
import android.transition.Slide;

import dagger.Provides;
import dagger.Module;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.util.SharedPrefs;

import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Contains methods for instantiating classes that get injected into activities
 * The same job every class suffixed with 'Module' has
 * Created by Antonio Martinović on 15.10.16.
 */
@Module
public class ApplicationModule {
    protected final MainApplication application;

    public ApplicationModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    MainApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    SharedPrefs provideSharedPrefs() {
        return new SharedPrefs(application);
    }
}
