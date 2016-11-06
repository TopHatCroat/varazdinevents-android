package hr.foi.varazdinevents.places.events;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.ActivityComponent;
import hr.foi.varazdinevents.injection.modules.ActivityModule;
import hr.foi.varazdinevents.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = MainApplication.get(this).getComponent()
                    .plus(new ActivityModule(this));
        }
        return activityComponent;
    }
}
