package hr.foi.varazdinevents.places.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import javax.inject.Inject;

import hr.foi.varazdinevents.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Valentin Magdić on 11/11/16.
 */

public class SettingsFragment extends PreferenceFragment {
//    public static final String PREF_LANGUAGE = "PREF_LANGUAGE";

    /**
     * Creates settings fragment which gets selected language
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        Preference languagePreference = findPreference("pref_key_language");

        languagePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                preferences.getString("weightPref", null);

                return true;
            }
        });
    }
}
