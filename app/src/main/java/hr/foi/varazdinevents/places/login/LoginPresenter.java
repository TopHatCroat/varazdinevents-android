package hr.foi.varazdinevents.places.login;

import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

public class LoginPresenter extends BasePresenter<LoginActivity> {
    UserManager userManager;

    public LoginPresenter(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void itemClicked(Object item) {

    }

    public void tryLogin(String username, String password){
        getViewLayer().showLoading(true);
        if (username != "" )
            getViewLayer().onSuccess();
        else
            getViewLayer().onFailure("Username treba biti unesen");
    }
}
