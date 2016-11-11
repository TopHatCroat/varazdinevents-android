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

    public void tryRegister (String email, String password, String password2) {

        if (!password.equals(password2)) {
            getViewLayer().showBasicError("Lozinke se ne podudaraju");
        } else if (email.length()==0 || password.length()==0 || password2.length()==0) {
            getViewLayer().showBasicError("Obavezno popunite sva polja");
        }
        else{
            long id = User.count(User.class) + 1;
            User user = new User(id, email,  password);
            user.save();
            //getViewLayer().showBasicError("Registracija uspješna. Prijavite se!");
            getViewLayer().onSuccess();
        }
    }
}
