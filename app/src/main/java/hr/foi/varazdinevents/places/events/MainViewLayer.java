package hr.foi.varazdinevents.places.events;

import com.google.common.collect.ImmutableList;

import java.util.List;

import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 12.10.16.
 */

public interface MainViewLayer extends ViewLayer {

    void showEvents(List<Event> events);
}
