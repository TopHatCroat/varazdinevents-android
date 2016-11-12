package hr.foi.varazdinevents.places.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hr.foi.varazdinevents.R;

/**
 * Created by sargon on 11/11/16.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
