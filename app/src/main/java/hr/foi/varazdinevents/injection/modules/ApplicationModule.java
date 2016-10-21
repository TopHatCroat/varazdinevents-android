package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import dagger.Provides;
import dagger.Module;

import javax.annotation.Priority;
import javax.inject.Singleton;

/**
 * Created by Antonio Martinović on 15.10.16.
 */
@Module
public class ApplicationModule {
    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

}
