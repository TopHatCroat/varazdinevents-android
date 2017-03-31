package hr.foi.varazdinevents.places.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.LoginActivityModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.register.RegisterActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.util.SharedPrefs;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

/**
 * Contains methods related to logging into application
 */
public class LoginActivity extends BaseActivity implements LoginViewLayer {
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.TFusername)
    TextView username;
    @BindView(R.id.TFpassword)
    TextView password;
    @Inject
    LoginPresenter presenter;

//    private LoginButton fbLoginButton;

    /**
     * Creates "Login" activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.login);
        if (User.count(User.class) < 2) {
            User user = new User("a", "a", "a");
            user.save();
            User user2 = new User("", "", "");
            user2.save();
        }

        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    /**
     * Crates animation on screen transition
     */
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    /**
     * Returns "Login" activity layout
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupActivityComponent() {
        //Uncomment those lines do measure dependencies creation time
        //Debug.startMethodTracing("SplashTrace");
        MainApplication.get(this)
                .getApplicationComponent()
                .plus(new LoginActivityModule(this))
                .inject(this);
        //Debug.stopMethodTracing();
    }

    public void showLoading(boolean loading) {
        // !!!!!!
    }

    /**
     * If login is successful, starts "Main" activity
     * @param user
     */
    @Override
    public void onSuccess(User user) {
        showLoading(false);
        MainApplication.get(this).createUserComponent(user);
        MainActivity.start(this);
        this.finish();
    }

    /**
     * If login is not successful, shows error message
     * @param message
     */
    @Override
    public void onFailure(String message) {
        showLoading(false);
        showBasicError(message);
    }

    /**
     * If the login button is clicked, gets entered username and password
     * and calls method for login
     */
    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        String user_username = username.getText().toString();
        String user_pass = password.getText().toString();
        showLoading(true);
        presenter.tryLogin(user_username, user_pass);
    }

    /**
     * If the sign up button is clicked, starts new activity for registration
     */
    @OnClick(R.id.signup_button)
    public void onSignUpButtonClicked() {
        RegisterActivity.start(this);
    }

    /**
     * If the cancel button is clicked, returns to the main screen
     */
    @OnClick(R.id.cancel_button)
    public void onCancelButtonClicked() {
        MainActivity.start(this);
    }

    /**
     * Starts "Login" activity
     * @param startingActivity
     */
    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, LoginActivity.class);
        startingActivity.startActivity(intent);
    }

}
