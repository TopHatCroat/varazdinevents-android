package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsPresenter;

/**
 * Created by Antonio Martinović on 08.11.16.
 */
@Module
public class EventDetailsActivityModule {
    private Activity activity;
    private EventDetailsPresenter presenterLayer;

    public EventDetailsActivityModule(Activity activity) {
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
    EventDetailsPresenter provideEventDetailsPresenter(User user){
        this.presenterLayer = new EventDetailsPresenter(user);
        return this.presenterLayer;
    }

}
