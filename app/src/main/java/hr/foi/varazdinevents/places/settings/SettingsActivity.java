package hr.foi.varazdinevents.places.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

import java.util.Locale;

import javax.inject.Inject;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.models.User;

import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

import static hr.foi.varazdinevents.util.Constants.PREF_LANG_KEY;

/**
 * Created by Antonio Martinović on 12.11.16.
 */
public class SettingsActivity extends BaseActivity implements ViewLayer, SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    User user;
    @Inject
    SettingsFragment settingsFragment;
    private String storePrefs;
    private SharedPreferences prefs;
    private ListPreference listPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, settingsFragment).commit();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        storePrefs = prefs.getString("Language", "German");

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this)
                .getUserComponent()
                .plus(new SettingsActivityModule(this))
                .inject(this);
    }

    @Override
    public void showBasicError(String message) {

    }

    @Override
    public void onItemClicked(Object item) {

    }

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, SettingsActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_LANG_KEY)) {
            changeLang(key);
            String s = sharedPreferences.getString(PREF_LANG_KEY, "");
            changeLang(s);
        }
        // etc
    }

    public void loadLocale() {
        String langPref = PREF_LANG_KEY;
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void saveLocale(String lang) {
        String langPref = PREF_LANG_KEY;
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }
}
