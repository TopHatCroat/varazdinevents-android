package hr.foi.varazdinevents.places.register;

import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
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

    public void tryRegister (String username, String email, String password, String password2) {

        if (!email.matches("[a-zA-Z][a-zA-Z0-9.]{1,}[a-zA-Z0-9]{1}[@]{1}[a-zA-Z]{1}[a-zA-Z.-]{2,}[a-zA-Z]")) {
            getViewLayer().showBasicError("Neispravan email");
        } else if (!password.equals(password2)) {
            getViewLayer().showBasicError("Lozinke se ne podudaraju");
        } else if (username.length() ==0 || email.length()==0 || password.length()==0 || password2.length()==0) {
            getViewLayer().showBasicError("Obavezno popunite sva polja");
        }
        else{
            User user = new User(null, username, email, password, null, null, null, null, null, null, null, null);
            user.save();
            //getViewLayer().showBasicError("Registracija uspješna. Prijavite se!");
            getViewLayer().onSuccess();
        }
    }
}
