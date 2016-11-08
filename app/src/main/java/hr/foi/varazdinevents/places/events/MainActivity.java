package hr.foi.varazdinevents.places.events;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.injection.MainActivityComponent;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {
    protected MainActivityComponent mainActivityComponent;

    protected int getLayout() {
        return R.layout.activity_main;
    }

//    @Override
    public MainActivityComponent getMainActivityComponent() {
        if (mainActivityComponent == null) {
            mainActivityComponent = MainApplication.get(this).getComponent()
                    .plus(new MainActivityModule(this));
        }
        return mainActivityComponent;
    }
}
