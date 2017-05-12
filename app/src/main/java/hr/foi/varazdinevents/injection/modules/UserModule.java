package hr.foi.varazdinevents.injection.modules;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.CityManager;
import hr.foi.varazdinevents.api.ImgurService;
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
    EventManager provideEventManager(User user, @Named("vzservice") RestService restService,
                                     @Named("imgurservice") ImgurService imgurService,
                                     SharedPrefs sharedPrefs) {
        return new EventManager(user, restService, imgurService, sharedPrefs);
    }

    @Provides
    @UserScope
    public CityManager provideCityManager(User user, @Named("vzservice") RestService restService, SharedPrefs sharedPrefs) {
        return new CityManager(user, restService, sharedPrefs);
    }
}
