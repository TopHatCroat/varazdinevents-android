package hr.foi.varazdinevents.places.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * Created by Antonio Martinović on 09.11.16.
 */

public class LoginActivity extends BaseActivity implements LoginViewLayer {
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.TFemail)
    TextView email;
    @BindView(R.id.TFpassword)
    TextView password;
    @Inject
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (User.count(User.class) < 2) {
            User user = new User(2, "test", "test");
            user.save();
            User user2 = new User(3, "", "");
            user2.save();
        }

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

    @Override
    public boolean isWithNavigation() {
        return false;
    }

    @Override
    public void onItemClicked(Object item) {
        // !!!!!!
    }

    public void showLoading(boolean loading) {
        // !!!!!!
    }

    @Override
    public void onSuccess() {
        showLoading(false);
        MainActivity.start(this);
    }

    @Override
    public void onFailure(String message) {
        showLoading(false);
        showBasicError(message);
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        String user_email = email.getText().toString();
        String user_pass = password.getText().toString();
        showLoading(true);
        presenter.tryLogin(user_email, user_pass);
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

}
