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
    @Nullable
    Fade provideFade() {
        Fade fade = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            fade = new Fade();
            fade.setDuration(1000);
        }
        return fade;
    }

    @Provides
    @Nullable
    Slide provideSlide() {
        Slide slide = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            slide = new Slide();
            slide.setDuration(1000);
        }
        return slide;
    }

    @Provides
    @Singleton
    SharedPrefs provideSharedPrefs() {
        return new SharedPrefs(application);
    }
}
