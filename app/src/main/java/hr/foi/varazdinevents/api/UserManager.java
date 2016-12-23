package hr.foi.varazdinevents.api;

import java.util.ArrayList;
import java.util.List;

import hr.foi.varazdinevents.api.responses.UserResponse;
import hr.foi.varazdinevents.models.User;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

/**
 * Contains all methods for working with users such as log in or creating new user
 */
public class UserManager {
    private RestService restService;
    private User user;

    /**
     * @param restService reference to Retrofit interface with API calls defined
     */
    public UserManager(RestService restService) {
        this.restService = restService;
    }

    /**
     * Returns a placeholder user for testing
     * @param username name of the user
     * @return User class with all data set to @param username
     */
    public static User getStubUser(String username) {
        return new User(username, username, username + "@foi.hr");
    }

    /**
     * Checks for user presence in the local DB and calls API for user log in
     * @param username
     * @param password
     * @return User class with token added
     */
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

    /**
     * Logs the user with @param token out
     * @param token user token
     * @return true if log out is successful, otherwise false
     */
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


    public Observable<String> registerFCMToken(String token) {
        return restService.sendFCMToken(token)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
