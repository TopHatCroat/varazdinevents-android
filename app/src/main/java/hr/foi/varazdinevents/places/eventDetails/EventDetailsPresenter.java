package hr.foi.varazdinevents.places.eventDetails;

import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Antonio Martinović on 08.11.16.
 */
public class EventDetailsPresenter extends BasePresenter<EventDetailsActivity>{

    private User user;

    public EventDetailsPresenter(User user) {
        this.user = user;
    }

    @Override
    public void itemClicked(Object item) {

    }

    public void itemFavorited(){

    }


}
