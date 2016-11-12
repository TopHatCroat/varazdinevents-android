package hr.foi.varazdinevents.places.login;

import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
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

    public void tryLogin(String username, String password) {
        List<User> user_list = new ArrayList<>();
        long count = User.count(User.class);
        if (count > 0) {
            user_list = User.find(User.class, "email= ? and password= ? ", username, password);
            if (user_list.size() == 0) {
                getViewLayer().showBasicError("Krivi email ili lozinka");
            } else {
                getViewLayer().onSuccess();
            }
        } else {
            getViewLayer().showBasicError("Nema zapisa u bazi");
        }
    }

}
