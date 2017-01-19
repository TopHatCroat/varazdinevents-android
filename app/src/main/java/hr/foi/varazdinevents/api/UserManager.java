package hr.foi.varazdinevents.api;

import android.support.test.espresso.core.deps.guava.base.Strings;

import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.SugarConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.foi.varazdinevents.api.responses.UserResponse;
import hr.foi.varazdinevents.api.responses.UserResponseComplete;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.util.SharedPrefs;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static hr.foi.varazdinevents.util.Constants.LAST_UPDATE_TIME_KEY;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

/**
 * Contains all methods for working with users such as log in or creating new user
 */
public class UserManager {
    private RestService restService;
    private User user;
    private SharedPrefs sharedPrefs;

    private List<User> users;
    /**
     * @param restService reference to Retrofit interface with API calls defined
     */
    public UserManager(RestService restService, SharedPrefs sharedPrefs) {
        this.restService = restService;
        this.sharedPrefs = sharedPrefs;
        users = new ArrayList<>();
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
        return restService.loginUser(username, password)
                .map(new Func1<UserResponse, User>() {
                    @Override
                    public User call(UserResponse userResponse) {
                        User user = new User(username, null);
                        user.setUsername(username);
                        user.setEmail(userResponse.email);
                        user.setApiId(userResponse.id);
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
        return restService.logoutUser(token)
                .map(new Func1<UserResponse, Boolean>() {
                    @Override
                    public Boolean call(UserResponse userResponse) {
                        UserManager.this.user.setToken(null);
                        user.save();
                        return Boolean.TRUE;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<JSONObject> registerFCMToken(String token) {
        return restService.sendFCMToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<User>> getUsers() {
        return Observable.concat(fromMemory(), fromDatabase(), fromNetwork())
                .cache()
                .map(new Func1<List<User>, List<User>>() {
                    @Override
                    public List<User> call(List<User> users) {
                        toMemory(users);
                        return UserManager.this.users;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private Observable<List<User>> fromMemory(){
        return Observable.just(users).doOnNext(new Action1<List<User>>() {
            @Override
            public void call(List<User> users) {
                Timber.w("Loading from memory...");
            }
        });
    }

    private Observable<List<User>> fromDatabase() {
        return Observable.just(
                User.listAll(User.class)
                //Select.from(Event.class).where(Condition.prop("DATE_TO").lt(System.currentTimeMillis()/1000)).list())
        )
                .doOnNext(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> users) {
                        Timber.w("Loading from database...");
                        toMemory(users);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<User>> fromNetwork() {
        String lastUpdateValue = String.valueOf(SharedPrefs.read(LAST_UPDATE_TIME_KEY, 0));

        return restService.getUsers(lastUpdateValue)
                .map(new Func1<UserResponseComplete, List<User>>() {
                    @Override
                    public List<User> call(UserResponseComplete userResponses) {
                        List<User> users = new LinkedList<>();
                        for(UserResponse userResponse : userResponses.items){
                            User user = new User();
                            user.setApiId(userResponse.id);
                            user.setUsername(userResponse.username);
                            user.setEmail(userResponse.email);
//                            user.setPassword(userResponse.password);
//                            user.setToken(userResponse.token);
                            user.setImage(userResponse.image);
                            user.setDescription(userResponse.description);
                            user.setWorkingTime(userResponse.workingTime);
                            user.setAddress(userResponse.address);
                            user.setFacebook(userResponse.facebook);
                            user.setWeb(userResponse.web);
                            user.setPhone(userResponse.phone);
                            users.add(user);
                        }
                        return users;
                    }
                })
                .doOnNext(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> users) {
                        Timber.w("Loading from REST...");
                        toMemory(users);
                        toDatabase(users);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void toMemory(List<User> users) {
        Timber.w("Saving to memory...");

        Map<Integer, User> usersMap = new HashMap<>();
        for (User user : this.users) {
            usersMap.put(user.apiId, user);
        }
        for (User user : users) {
            if(!usersMap.containsKey(user.apiId))
                usersMap.put(user.apiId, user);
        }

        this.users = new ArrayList<User>(usersMap.values());
    }

    private void toDatabase(List<User> users) {
        Timber.w("Saving to database...");
        User tmp;
        for (User user : this.users) {
            tmp = Select.from(User.class)
                    .where(Condition.prop("API_ID").eq(user.apiId))
                    .first();
            if(tmp == null) {
                User.save(user);
            }
        }
    }

    public static void logout() {
        Iterator<User> users = SugarRecord.findAll(User.class);
        while(users.hasNext()) {
            User user = users.next();
            user.setToken("");
            user.save();
        }
    }

    public static User getLoggedInUser() {
        List<User> users = User.find(User.class, "token != '' ");
        if (users.size() > 0) {
            if( ! Strings.isNullOrEmpty(users.get(0).getToken())){
                return users.get(0);
            }
        }
        return getStubUser("test");
    }
}