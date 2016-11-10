package hr.foi.varazdinevents.places.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.modules.RegisterActivityModule;
import hr.foi.varazdinevents.ui.base.BaseActivity;

/**
 * Created by Bruno on 10.11.16.
 */
public class RegisterActivity extends BaseActivity {

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

    @Override
    public void onItemClicked(Object item) {

    }

    @Override
    public boolean isWithNavigation() {
        return false;
    }

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, RegisterActivity.class);
        startingActivity.startActivity(intent);
    }
}
