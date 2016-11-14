package hr.foi.varazdinevents.places.login;

import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import rx.Observer;

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
        long count = User.count(User.class);
        if (count > 0) {
            userManager.login(username, password).subscribe(new Observer<User>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getViewLayer().onFailure(getViewLayer().getResources().getString(R.string.loginError));
                    getViewLayer().onFailure("asdasdasdada");
                }

                @Override
                public void onNext(User user) {
                    getViewLayer().onSuccess(user);
                }
            });
//            if (user_list.size() == 0) {
//                getViewLayer().showBasicError(getViewLayer().getResources().getString(R.string.loginError));
//            } else {
//                getViewLayer().onSuccess(user_list.get(0));
//            }
        } else {
            getViewLayer().showBasicError(getViewLayer().getResources().getString(R.string.loginDatabaseError));
        }
    }

}
