package hr.foi.varazdinevents.places.login;

import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.ui.base.ViewLayer;

/**
 * Created by Antonio Martinović on 10.11.16.
 */

public interface LoginViewLayer extends ViewLayer {
    void onSuccess(User user);
    void onFailure(String message);

}
