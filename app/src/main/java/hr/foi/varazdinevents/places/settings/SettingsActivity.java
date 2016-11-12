package hr.foi.varazdinevents.places.settings;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;

import hr.foi.varazdinevents.places.register.RegisterActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 12.11.16.
 */
public class SettingsActivity extends PreferenceActivity implements ViewLayer {


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
}
