package hr.foi.varazdinevents.injection.modules;

import android.app.Application;
import android.content.Context;
import dagger.Provides;
import dagger.Module;
import hr.foi.varazdinevents.MainApplication;

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

//    @Provides
//    @Singleton
//    Context provideContext() {
//        return application;
//    }

}
