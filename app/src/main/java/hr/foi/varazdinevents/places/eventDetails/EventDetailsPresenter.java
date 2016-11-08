package hr.foi.varazdinevents.places.eventDetails;

import hr.foi.varazdinevents.models.Event;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Antonio Martinović on 08.11.16.
 */
public class EventDetailsPresenter extends BasePresenter<EventDetailsView>{

    private final Event event;

    public EventDetailsPresenter(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }

    @Override
    public void itemClicked(Object item) {

    }
}
