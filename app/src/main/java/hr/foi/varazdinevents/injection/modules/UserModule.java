package hr.foi.varazdinevents.injection.modules;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.EventManager;
import hr.foi.varazdinevents.api.RestService;
import hr.foi.varazdinevents.injection.UserScope;
import hr.foi.varazdinevents.models.User;
import hr.foi.varazdinevents.util.SharedPrefs;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
@Module
public class UserModule {

    private User user;

    public UserModule(User user) {
        this.user = user;
    }

    @Provides
    @UserScope
    User provideUser() {
        return user;
    }

    @Provides
    @UserScope
    EventManager provideEventManager(User user, RestService restService, SharedPrefs sharedPrefs) {
        return new EventManager(user, restService, sharedPrefs);
    }
}
