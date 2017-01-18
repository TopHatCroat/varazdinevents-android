package hr.foi.varazdinevents.places.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.ui.base.BaseNavigationActivity;


public class AboutActivity extends BaseNavigationActivity {

    @Inject
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.about_app);
    }

    @Override
    protected User getUser() {
        return user;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void setupActivityComponent() {

    }

    public static void start(Context startingActivity) {
        Intent intent = new Intent(startingActivity, AboutActivity.class);
        startingActivity.startActivity(intent);
    }
}
