package hr.foi.varazdinevents.places.login;

import android.os.Bundle;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;

import java.net.UnknownHostException;
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

    public void tryLogin(String username, String password) {
        getViewLayer().showLoading(true);
        userManager.login(username, password).subscribe(new Observer<User>() {
            @Override
            public void onCompleted() {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                userManager.registerFCMToken(refreshedToken).subscribe();

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    getViewLayer().onFailure(getViewLayer().getResources().getString(R.string.network_not_accessible));
                }
                getViewLayer().onFailure(getViewLayer().getResources().getString(R.string.loginError));
            }

            @Override
            public void onNext(User user) {
                if(Strings.isNullOrEmpty(user.getToken())) {
                    getViewLayer().onFailure(getViewLayer().getResources().getString(R.string.loginError));
                } else {
                    getViewLayer().onSuccess(user);
                }
            }
        });
    }

}
