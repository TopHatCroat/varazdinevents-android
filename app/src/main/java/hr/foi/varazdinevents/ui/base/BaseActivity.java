package hr.foi.varazdinevents.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.Unbinder;
import hr.foi.varazdinevents.injection.ActivityComponent;
import hr.foi.varazdinevents.injection.ActivityComponentFactory;
import hr.foi.varazdinevents.injection.ApplicationComponent;
import hr.foi.varazdinevents.util.BundleService;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected ActivityComponent activityComponent;
    private BundleService bundleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
    }

    protected abstract int getLayout();

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = ActivityComponentFactory.create(this);
        }
        return activityComponent;
    }

    public BundleService getBundleService() {
        return bundleService;
    }

}
