package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.hostProfile.HostProfilePresenter;

/**
 * Created by Valentin Magdić on 26.12.16..
 */
@Module
public class HostProfileActivityModule {
    private Activity activity;
    private HostProfilePresenter presenterLayer;

    public HostProfileActivityModule(Activity activity) {
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
    HostProfilePresenter provideHostProfilePresenter(User user){
        this.presenterLayer = new HostProfilePresenter(user);
        return this.presenterLayer;
    }
}
