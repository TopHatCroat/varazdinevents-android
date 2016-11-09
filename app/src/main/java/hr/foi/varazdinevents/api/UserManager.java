package hr.foi.varazdinevents.api;

import hr.foi.varazdinevents.models.User;
import rx.Observable;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
public class UserManager {
    private RestService restService;

    public UserManager(RestService restService) {
        this.restService = restService;
    }

    public static User getStubUser(String username) {
        return new User(0, username, username + "@foi.hr");
    }
}
