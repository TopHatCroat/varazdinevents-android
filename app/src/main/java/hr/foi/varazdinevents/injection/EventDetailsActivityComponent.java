package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.BundleModule;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsView;

/**
 * Created by Antonio Martinović on 08.11.16.
 */
@Subcomponent(modules = {
        EventDetailsActivityModule.class, BundleModule.class})
@ActivityScope
public interface EventDetailsActivityComponent {
    void inject(EventDetailsView eventDetailsView);

}
