package hr.foi.varazdinevents.injection.modules;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
//import android.support.test.espresso.core.deps.dagger.Module;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntKey;
import dagger.multibindings.IntoMap;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsPresenter;
import hr.foi.varazdinevents.places.events.BasicEventViewHolderFactory;
import hr.foi.varazdinevents.places.events.NoImageEventViewHolderFactory;
import hr.foi.varazdinevents.places.hostProfile.HostProfilePresenter;
import hr.foi.varazdinevents.ui.elements.ItemViewHolderFactory;
import hr.foi.varazdinevents.ui.elements.list.ItemListAdapter;

/**
 * Created by Valentin Magdić on 26.12.16..
 */
@Module
public class HostProfileActivityModule {
    private Activity activity;
    private HostProfilePresenter presenterLayer;


    public HostProfileActivityModule(Activity activity) {
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
    HostProfilePresenter provideHostProfilePresenter(UserManager userManager, EventManager eventManager){
        this.presenterLayer = new HostProfilePresenter(userManager, eventManager);
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
