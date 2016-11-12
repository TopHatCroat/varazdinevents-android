package hr.foi.varazdinevents.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.places.settings.SettingsActivity;
import hr.foi.varazdinevents.util.BundleService;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity implements ViewLayer, NavigationView.OnNavigationItemSelectedListener  {
//    protected MainActivityComponent mainActivityComponent;
    private BundleService bundleService;
    protected Unbinder unbinder;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Nullable
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();

        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);


        if(isWithNavigation()) {
            setSupportActionBar(toolbar);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();


            navigationView.setNavigationItemSelectedListener(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected abstract int getLayout();

    protected abstract void setupActivityComponent();

    @Override
    public void showBasicError(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public BundleService getBundleService() {
        return bundleService;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                SettingsActivity.start(this);
                break;
            case R.id.menu_about:
//                AboutActivity.start(this);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(isWithNavigation()) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    public boolean isWithNavigation() {
        return true;
    }
}
