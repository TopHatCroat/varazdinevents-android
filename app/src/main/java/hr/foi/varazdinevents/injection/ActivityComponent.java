package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.ActivityModule;
import hr.foi.varazdinevents.injection.modules.BundleModule;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.places.events.MainView;
import hr.foi.varazdinevents.ui.elements.ItemRecyclerView;
import hr.foi.varazdinevents.ui.elements.ItemViewHolder;

/**
 * Created by Antonio Martinović on 15.10.16.
 */

@Subcomponent(modules = {
        ActivityModule.class, BundleModule.class})
@ActivityScope
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(ItemRecyclerView itemRecyclerView);
    void inject(MainView mainView);
//    void inject(ItemViewHolder itemViewHolder);

}
