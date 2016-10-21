package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.ui.base.BaseActivity;
import hr.foi.varazdinevents.util.BundleService;


@Module
public class BundleModule {

    @Provides
    public Bundle provideBundle(Activity context) {
        return context.getIntent().getExtras() == null ? new Bundle() : context.getIntent().getExtras();
    }

    @Provides
    public Intent provideIntent(Activity context) {
        return context.getIntent() == null ? new Intent() : context.getIntent();
    }

    @Provides
    @ActivityScope
    public BundleService provideBundleService(Activity context) {
        return ((BaseActivity) context).getBundleService();
    }

//    @RouteId
//    @ActivityScope
//    @Provides
//    Long provideRouteLongId(BundleService bundleService) {
//        return (Long) bundleService.get(EXTRA_ROUTE);
//    }

}
