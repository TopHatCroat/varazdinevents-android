package hr.foi.varazdinevents.places.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import javax.inject.Inject;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.models.User;
import javax.inject.Inject;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.RegisterActivityModule;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.places.register.RegisterActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

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
    public boolean isWithNavigation() {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_key_language")) {
            storePrefs = (prefs.getString("Language", "German"));
        }
        // etc
    }
}
