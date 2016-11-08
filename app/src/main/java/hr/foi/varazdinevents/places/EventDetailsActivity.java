package hr.foi.varazdinevents.places;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.MainActivityComponent;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.ui.base.BaseActivity;

/**
 * Created by Antonio Martinović on 08.11.16.
 */

public class EventDetailsActivity extends BaseActivity {
    protected MainActivityComponent mainActivityComponent;

    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupActivityComponent() {

    }

}
