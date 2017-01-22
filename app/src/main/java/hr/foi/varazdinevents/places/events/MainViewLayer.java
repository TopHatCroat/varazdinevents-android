package hr.foi.varazdinevents.places.events;

import java.util.List;

import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Additional methods for {@link MainActivity}
 * Created by Antonio Martinović on 12.10.16.
 */
public interface MainViewLayer extends ViewLayer {

    /**
     * Creates all necessary stuff to replace all events
     * @param events new events to show
     */
    void showEvents(List<Event> events);
}
