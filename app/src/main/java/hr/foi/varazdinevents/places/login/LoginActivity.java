package hr.foi.varazdinevents.places.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.LoginActivityModule;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

public class LoginActivity extends BaseActivity implements LoginViewLayer {
    @BindView(R.id.login_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           /* User user = new User(1, "brumihali@foi.hr", "123");
            user.save();
            */
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
        // !!!!!!!!!!!!!!
    }

    public void showLoading(boolean loading) {

    }

    @Override
    public void onSuccess() {
        MainActivity.start(this);
    }

    @Override
    public void onFailure(String message) {
        showBasicError(message);
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClicked() {
        MainActivity.start(this);
    }

}
