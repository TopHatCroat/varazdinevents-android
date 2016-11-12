package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.places.settings.SettingsActivity;

/**
 * Created by Antonio Martinović on 12.11.16.
 */
@ActivityScope
@Subcomponent(
        modules = SettingsActivityModule.class
)
public interface SettingsActivityComponent {
    SettingsActivity inject(SettingsActivity settingsActivity);
}
