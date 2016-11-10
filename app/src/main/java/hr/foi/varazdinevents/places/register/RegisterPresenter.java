package hr.foi.varazdinevents.places.register;

import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.ui.base.BasePresenter;

/**
 * Created by Bruno on 10.11.16.
 */
public class RegisterPresenter extends BasePresenter<RegisterActivity>{
    private UserManager userManager;

    public RegisterPresenter(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void itemClicked(Object item) {

    }
}
