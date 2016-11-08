package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.injection.modules.BundleModule;
import hr.foi.varazdinevents.places.events.MainActivity;

/**
 * Created by Antonio Martinović on 15.10.16.
 */

@Subcomponent(modules = {
        MainActivityModule.class, BundleModule.class})
@ActivityScope
public interface MainActivityComponent {

    MainActivity inject(MainActivity mainActivity);

//    void inject(ItemViewHolder itemViewHolder);

}
