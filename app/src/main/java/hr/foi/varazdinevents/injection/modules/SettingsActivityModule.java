package hr.foi.varazdinevents.injection.modules;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.settings.SettingsActivity;
import hr.foi.varazdinevents.places.settings.SettingsFragment;
import hr.foi.varazdinevents.places.settings.SettingsPresenter;

/**
 * Created by Antonio Martinović on 12.11.16.
 */
@Module
public class SettingsActivityModule {
    private SettingsActivity settingsActivity;

    public SettingsActivityModule(SettingsActivity settingsActivity) {
        this.settingsActivity = settingsActivity;
    }

    @Provides
    @ActivityScope
    SettingsActivity provideSettingsActivity() {
        return settingsActivity;
    }

    @Provides
    @ActivityScope
    SettingsPresenter
    provideSettingsPresenter() {
        return new SettingsPresenter();
    }

    @Provides
    @ActivityScope
    SettingsFragment provideSettingsFragment(){
        return new SettingsFragment();
    }
}
