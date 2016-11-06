package hr.foi.varazdinevents.places.events;

import com.google.common.collect.ImmutableList;

import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public interface MainViewLayer extends ViewLayer {

    void showEvents(ImmutableList<Event> events);
}
