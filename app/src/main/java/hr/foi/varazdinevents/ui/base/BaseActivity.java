package hr.foi.varazdinevents.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hr.foi.varazdinevents.util.BundleService;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public abstract class BaseActivity extends AppCompatActivity {
//    protected MainActivityComponent mainActivityComponent;
    private BundleService bundleService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
    }

    protected abstract int getLayout();

//    public abstract void getMainActivityComponent();

    public BundleService getBundleService() {
        return bundleService;
    }

}
