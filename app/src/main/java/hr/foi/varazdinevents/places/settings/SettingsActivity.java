package hr.foi.varazdinevents.places.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.EditText;
import java.util.Locale;
import javax.inject.Inject;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.fcm.MessagingService;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

import static hr.foi.varazdinevents.util.Constants.PREF_LANG_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_NOTIFICATIONS_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_USERNAME_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_PASSWORD_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_EMAIL_KEY;

/**
 * Created by Antonio Martinović on 12.11.16.
 */

/**
 * Contains all settings related activities in the application
 */
public class SettingsActivity extends BaseActivity implements ViewLayer, SharedPreferences.OnSharedPreferenceChangeListener {
    @Inject
    User user;
    @Inject
    SettingsFragment settingsFragment;

    private String dbUserDetails;
    private String storePrefs;
    private SharedPreferences prefs;
    private ListPreference listPreference;

    /**
     * Creates "Settings" activity.
     * Gets default shared preferences and sets default values
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, settingsFragment).commit();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        storePrefs = prefs.getString("Language", "English");

        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);
//        EditText username = (EditText) findViewById(R.id.pref_id_username);
//        username.setText(user.getUsername());

//        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager().findPreference("checkboxPref");
//
//        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());
//                return true;
//            }
//        });
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startingActivity.startActivity(intent);
    }

    /**
     * Checks which of the preferences was clicked, gets new input and updates values
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_LANG_KEY)) {
            changeLang(key);
            String s = sharedPreferences.getString(PREF_LANG_KEY, "");
            changeLang(s);
        }
        if(key.equals(PREF_NOTIFICATIONS_KEY)){
            MessagingService.allowNotifications = !MessagingService.allowNotifications;
        }
    }

    public void loadLocale() {
        String langPref = PREF_LANG_KEY;
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    /**
     * Edits new selected language
     * @param lang
     */
    public void saveLocale(String lang) {
        String langPref = PREF_LANG_KEY;
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    /**
     * Changes default local application language
     * @param lang
     */
    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SettingsActivity.start(this);
    }

    /**
     * Prompts dialog with received parameters
     * @param title
     * @param message
     */
    private void alertView(String title, String message ) {
        AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

//    @OnClick(R.id.pref_id_username)
//    public void onUsernameClicked(){
//        test();
//    }

    @Override
    public void onBackPressed() {
        MainActivity.start(this);
    }

}
