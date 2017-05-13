package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import hr.foi.varazdinevents.api.CityManager;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.FestivalManager;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.events.BasicEventViewHolderFactory;
import hr.foi.varazdinevents.places.events.MainPresenter;

import hr.foi.varazdinevents.places.events.NoImageEventViewHolderFactory;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;
import hr.foi.varazdinevents.ui.elements.ItemViewHolderFactory;

/**
 * Created by Antonio Martinović on 15.10.16.
 */
@Module
public class MainActivityModule {

    private Activity activity;
    private MainPresenter presenterLayer;

    public MainActivityModule(Activity activity) {
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
    MainPresenter provideMainPresenter(EventManager eventManager, UserManager userManager,
                                       CityManager cityManager, FestivalManager festivalManager){
        this.presenterLayer = new MainPresenter(eventManager, userManager, cityManager, festivalManager);
        return this.presenterLayer;
    }

    @Provides
    @ActivityScope
    ItemListAdapter provideEventListAdapter(Map<Integer, ItemViewHolderFactory> viewHolderFactoryMap) {
        return new ItemListAdapter(this.presenterLayer, viewHolderFactoryMap);
    }

    @Provides
    @ActivityScope
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }


    @Provides
    @ActivityScope
    GridLayoutManager provideGridLayoutManager(Context context) {
        return new GridLayoutManager(context, 2);
    }

    @Provides
    @IntoMap
    @IntKey(100)
    ItemViewHolderFactory provideBasicViewHolder() {
        return new BasicEventViewHolderFactory();
    }

    @Provides
    @IntoMap
    @IntKey(101)
    ItemViewHolderFactory provideNoImageViewHolder() {
        return new NoImageEventViewHolderFactory();
    }
}
