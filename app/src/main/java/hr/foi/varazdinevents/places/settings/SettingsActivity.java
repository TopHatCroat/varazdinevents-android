package hr.foi.varazdinevents.places.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

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
public class SettingsActivity extends BaseActivity implements ViewLayer {

    @Inject
    SettingsFragment settingsFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, settingsFragment).commit();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this)
                .getApplicationComponent()
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
}
