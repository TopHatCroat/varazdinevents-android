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
import hr.foi.varazdinevents.places.newEvent.NewEventActivity;
import hr.foi.varazdinevents.places.settings.SettingsActivity;
import hr.foi.varazdinevents.util.BundleService;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity implements ViewLayer {
    private BundleService bundleService;
    protected Unbinder unbinder;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;


    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupActivityComponent();
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);

        if(toolbar != null)
            setSupportActionBar(toolbar);

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

}
