package hr.foi.varazdinevents.places.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;


import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 12.11.16.
 */
public class SettingsActivity extends PreferenceActivity implements ViewLayer, SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    @BindView(R.id.btn_en)
    Button Btn_en;
    @BindView(R.id.btn_hr)
    Button Btn_hr;
    @BindView(R.id.btn_de)
    Button Btn_de;

    @Inject
    User user;
    @Inject
    SettingsFragment settingsFragment;
//    private String storePrefs;
//    private SharedPreferences prefs;
    private Button btn_en, btn_hr, btn_de;
    private Locale myLocale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_preferences);
        this.btn_en.setOnClickListener(this);
        this.btn_hr.setOnClickListener(this);
        this.btn_de.setOnClickListener(this);
        loadLocale();
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }
    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }
    private void updateTexts()
    {
        btn_en.setText(R.string.btn_en);
        btn_hr.setText(R.string.btn_hr);
        btn_de.setText(R.string.btn_de);
    }
    //@Override
    public void onClick(View v) {
        String lang = "en";
        switch (v.getId()) {
            case R.id.btn_en:
                lang = "en";
                break;
            case R.id.btn_hr:
                lang = "hr";
                break;
            case R.id.btn_de:
                lang = "de";
                break;
            default:
                break;
        }
        changeLang(lang);
    }
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }



//    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

//    @Override
//    protected void setupActivityComponent() {
//        MainApplication.get(this)
//                .getUserComponent()
//                .plus(new SettingsActivityModule(this))
//                .inject(this);
//    }

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

//    @Override
//    public boolean isWithNavigation() {
//        return false;
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals("pref_key_language")) {
//            storePrefs = (prefs.getString("Language", "German"));
//        }
        // etc
    }
}
