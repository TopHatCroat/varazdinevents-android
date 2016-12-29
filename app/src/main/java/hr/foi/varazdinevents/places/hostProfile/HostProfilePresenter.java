package hr.foi.varazdinevents.places.hostProfile;

import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

public class HostProfilePresenter extends BasePresenter<HostProfileActivity>{

    private User user;

    public HostProfilePresenter(User user){this.user = user;}

    @Override
    public void itemClicked(Object item) {

    }

}
