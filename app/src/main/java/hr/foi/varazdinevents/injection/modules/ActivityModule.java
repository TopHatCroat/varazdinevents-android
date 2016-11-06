package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.events.BasicEventViewHolderFactory;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.events.MainPresenter;
import hr.foi.varazdinevents.places.events.MainView;
import hr.foi.varazdinevents.ui.elements.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.ItemViewHolderFactory;

/**
 * Created by Antonio Martinović on 15.10.16.
 */
@Module
public class ActivityModule {

    private Activity activity;
    private MainView mainView;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityScope
    Context providesContext() {
        return activity;
    }

    @Provides
    MainPresenter provideMainPresenter(){
        return new MainPresenter();
    }

    @Provides
    @ActivityScope
    MainView provideMainView() {
        mainView = new MainView(activity);
        return mainView;
    }

    @Provides
    @ActivityScope
    ItemListAdapter provideEventListAdapter(Map<Integer, ItemViewHolderFactory> viewHolderFactoryMap) {
        return new ItemListAdapter(this.mainView, viewHolderFactoryMap);
    }

    @Provides
    @ActivityScope
    LinearLayoutManager provideLinearLayoutManager(MainActivity mainActivity) {
        return new LinearLayoutManager(mainActivity);
    }

    @Provides
    @IntoMap
    @IntKey(100)
    ItemViewHolderFactory provideTitleViewHolder() {
        return new BasicEventViewHolderFactory();
    }
}
