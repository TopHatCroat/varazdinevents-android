package hr.foi.varazdinevents.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Strings;

import javax.inject.Inject;

import butterknife.BindView;
import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.login.LoginActivity;
import hr.foi.varazdinevents.places.newEvent.NewEventActivity;
import hr.foi.varazdinevents.places.settings.SettingsActivity;

/**
 * Created by Antonio Martinović on 27.12.16.
 */

public abstract class BaseNavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.host_drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.host_navigation_view)
    protected NavigationView navigationView;

    @Inject
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        View hView =  navigationView.getHeaderView(0);
        TextView username = (TextView)hView.findViewById(R.id.user_username);
        TextView email = (TextView)hView.findViewById(R.id.user_email);
        if (Strings.isNullOrEmpty(user.getToken())) {
            username.setText(R.string.nav_header_title);
            email.setText(R.string.nav_header_info);
        }
        else {
            username.setText("Korisnik: " + user.getUsername());
            email.setText("Email: " + user.getEmail());
        }

        if( ! Strings.isNullOrEmpty(user.getToken())) {
            navigationView.getMenu().findItem(R.id.organizers_menu_option).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_logout).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.organizers_menu_option).setVisible(false);
            navigationView.getMenu().findItem(R.id.menu_login).setVisible(true);
        }

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


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
                MainApplication.get(this).createUserComponent(UserManager.getStubUser("test"));
                MainActivity.start(this);
                break;
            case R.id.menu_all_events:
                MainActivity.start(this);
                break;
        }
        return true;
    }

    @Deprecated
    protected abstract User getUser();
}
