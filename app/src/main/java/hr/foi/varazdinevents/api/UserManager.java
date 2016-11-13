package hr.foi.varazdinevents.api;

import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.models.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
public class UserManager {
    private RestService restService;
    private User user;

    public UserManager(RestService restService) {
        this.restService = restService;
    }

    public static User getStubUser(String username) {
        return new User(username, username, username + "@foi.hr");
    }

    public Observable<User> login(final String username, String password) {
        List<User> users = new ArrayList<>();
        users = User.find(User.class, "username= ? and password= ? ", username, password);
        if (users.size() > 0) {
            this.user = users.get(0);
        } else {
            this.user = new User(username, password);
        }
        return restService.loginUser(username, password)
                .map(new Func1<UserResponse, User>() {
                    @Override
                    public User call(UserResponse userResponse) {
                        user.setToken(userResponse.token);
                        user.save();
                        return user;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> logout(String token) {
        return restService.logutUser(token)
                .map(new Func1<UserResponse, Boolean>() {
                    @Override
                    public Boolean call(UserResponse userResponse) {
//                        User user = new User();
//                        user.setUsername(username);
//                        user.setToken(userResponse.token);
                        return Boolean.TRUE;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
