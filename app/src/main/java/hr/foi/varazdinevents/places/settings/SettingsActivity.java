package hr.foi.varazdinevents.places.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.models.User;

import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

import static hr.foi.varazdinevents.util.Constants.PREF_LANG_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_USERNAME_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_PASSWORD_KEY;
import static hr.foi.varazdinevents.util.Constants.PREF_EMAIL_KEY;

/**
 * Created by Antonio Martinović on 12.11.16.
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
        if(key.equals(PREF_USERNAME_KEY)){
            String username = sharedPreferences.getString(PREF_USERNAME_KEY, "");
            updateUserDetails(username, user.getPassword(), user.getEmail());
        }
        if(key.equals(PREF_PASSWORD_KEY)){
            String password = sharedPreferences.getString(PREF_PASSWORD_KEY, "");
            updateUserDetails(user.getUsername(), password, user.getEmail());
        }
        if(key.equals(PREF_EMAIL_KEY)){
            String email = sharedPreferences.getString(PREF_EMAIL_KEY, "");
            updateUserDetails(user.getUsername(), user.getPassword(), email);
        }
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

    public void updateUserDetails(String username, String password, String email){
        if (!email.matches("[a-zA-Z][a-zA-Z0-9.]{1,}[a-zA-Z0-9]{1}[@]{1}[a-zA-Z]{1}[a-zA-Z.-]{2,}[a-zA-Z]")) {
            alertView("Error", "Incorrect email!");
        }
        if(username.length()==0){
            alertView("Error", "Incorrect username!");
        }
          //+pass regex?
        else{
            User user = new User(null, username, email, password, null);
            user.save();
            alertView("Success", "Successfully updated!");
        }
    }

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

    String m_Text = "";
    public void test(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


}
