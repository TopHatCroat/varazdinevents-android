package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.facebook.FacebookPresenter;

/**
 * Created by Valentin Magdić on 23.01.17..
 */
@Module
public class FacebookActivityModule {

    private Activity activity;
    private FacebookPresenter presenterLayer;


    public FacebookActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    Context providesContext() {
        return activity;
    }

    @Provides
    FacebookPresenter provideFacebookPresenter(UserManager userManager, EventManager eventManager){
        this.presenterLayer = new FacebookPresenter(userManager);
        return this.presenterLayer;
    }

}
