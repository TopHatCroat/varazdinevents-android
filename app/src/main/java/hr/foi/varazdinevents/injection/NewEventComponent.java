package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.NewEventModule;
import hr.foi.varazdinevents.places.newEvent.NewEventActivity;

/**
 * Created by Antonio Martinović on 03.12.16.
 */

@Subcomponent(modules = NewEventModule.class)
public interface NewEventComponent {
    NewEventActivity inject(NewEventActivity newEventActivity);
}
