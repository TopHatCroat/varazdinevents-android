package hr.foi.varazdinevents.places.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

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
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;

/**
 * Created by Antonio Martinović on 09.11.16.
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

        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

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

    @Override
    public void onSuccess(User user) {
        showLoading(false);
        MainApplication.get(this).createUserComponent(user);
        MainActivity.start(this);
        this.finish();
    }

    @Override
    public void onFailure(String message) {
        showLoading(false);
        showBasicError(message);
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        String user_username = username.getText().toString();
        String user_pass = password.getText().toString();
        showLoading(true);
        presenter.tryLogin(user_username, user_pass);
    }

    @OnClick(R.id.signup_button)
    public void onSignUpButtonClicked() {
        RegisterActivity.start(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop(){
        super.onStop();
        presenter.detachView();
    }

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, LoginActivity.class);
        startingActivity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.app_exit)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
