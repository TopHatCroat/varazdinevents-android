package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;

/**
 * Created by Antonio Martinović on 08.11.16.
 */
@Subcomponent(modules = {
        EventDetailsActivityModule.class})
@ActivityScope
public interface EventDetailsActivityComponent {
    EventDetailsActivity inject(EventDetailsActivity eventDetailsActivity);

}
