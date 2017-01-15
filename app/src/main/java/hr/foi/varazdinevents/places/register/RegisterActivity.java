package hr.foi.varazdinevents.places.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.RegisterActivityModule;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.login.LoginActivity;
import hr.foi.varazdinevents.places.login.LoginViewLayer;
import hr.foi.varazdinevents.ui.base.BaseActivity;

/**
 * Created by Bruno on 10.11.16.
 */


public class RegisterActivity extends BaseActivity {
    @BindView(R.id.TFusername_register)
    TextView username;

    @BindView(R.id.TFemail_register)
    TextView email;

    @BindView(R.id.TFpass1_register)
    TextView pass1;

    @BindView(R.id.TFpass2_register)
    TextView pass2;

    @Inject
    RegisterPresenter presenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupActivityComponent() {
        MainApplication.get(this)
                .getApplicationComponent()
                .plus(new RegisterActivityModule(this))
                .inject(this);
    }

    public void showLoading(boolean loading) {
        // !!!!!!
    }

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, RegisterActivity.class);
        startingActivity.startActivity(intent);
    }

    public void onSuccess() {
        showLoading(false);
        LoginActivity.start(this);
        this.finish();
    }

    public void onFailure(String message) {
        showLoading(false);
        showBasicError(message);
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

    @OnClick(R.id.register_button)
    public void onRegisterButtonClicked() {
        String user_username = username.getText().toString();
        String user_email = email.getText().toString();
        String user_pass = pass1.getText().toString();
        String user_pass2 = pass2.getText().toString();
        showLoading(true);
        presenter.tryRegister(user_username, user_email, user_pass, user_pass2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
