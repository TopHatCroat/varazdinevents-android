package hr.foi.varazdinevents.injection.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.newEvent.NewEventActivity;
import hr.foi.varazdinevents.places.newEvent.NewEventPresenter;

/**
 * Created by Antonio Martinović on 03.12.16.
 */
@Module
public class NewEventModule {
    private NewEventActivity activity;
    private NewEventPresenter presenter;

    public NewEventModule(NewEventActivity activity) {
        this.activity = activity;
    }

    @Provides
    public NewEventActivity provideActivity() {
        return this.activity;
    }

    @Provides
    @ActivityScope
    public Context provideContext() {
        return this.activity;
    }

    @Provides
    public NewEventPresenter provideNewEventPresenter(EventManager eventManager) {
        this.presenter = new NewEventPresenter(eventManager);
        return presenter;
    }

}
