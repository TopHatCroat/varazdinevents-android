package hr.foi.varazdinevents.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import dagger.BindsOptionalOf;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.login.LoginActivity;
import hr.foi.varazdinevents.places.newEvent.NewEventActivity;
import hr.foi.varazdinevents.places.settings.SettingsActivity;

/**
 * Created by Antonio Martinović on 27.12.16.
 */

public abstract class BaseNavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Nullable
    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @Nullable
    @BindView(R.id.navigation_view)
    protected NavigationView navigationView;


    @Inject
    User user;
//    @BindView(R.id.user_username)
//    TextView username;
//    @BindView(R.id.user_email)
//    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

//        username.setText(user.getUsername());
//        email.setText(user.getPassword());
//
//        String username = user.getUsername();
//        String password = user.getPassword();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                SettingsActivity.start(this);
                break;
            case R.id.menu_create_event:
                NewEventActivity.start(this);
                break;
            case R.id.menu_about:
//                AboutActivity.start(this);
                break;
            case R.id.menu_login:
                LoginActivity.start(this);
                break;
            case R.id.menu_logout:
                MainActivity.start(this);
                break;
            case R.id.menu_all_events:
                MainActivity.start(this);
                break;
        }
        return true;
    }


    protected abstract User getUser();

}
