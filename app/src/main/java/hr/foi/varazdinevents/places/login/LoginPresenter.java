package hr.foi.varazdinevents.places.login;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.MainApplication;
import hr.foi.varazdinevents.R;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.places.events.MainActivity;
import hr.foi.varazdinevents.ui.base.BasePresenter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    userManager.registerFCMToken(refreshedToken).subscribe();
                }

                @Override
                public void onError(Throwable e) {
                    getViewLayer().onFailure(getViewLayer().getResources().getString(R.string.loginError));
                }

                @Override
                public void onNext(User user) {
                    getViewLayer().onSuccess(user);
                }
            });
        } else {
            getViewLayer().showBasicError(getViewLayer().getResources().getString(R.string.loginDatabaseError));
        }
    }

}
