package hr.foi.varazdinevents.injection.modules;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.login.LoginActivity;
import hr.foi.varazdinevents.places.login.LoginPresenter;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
@Module
public class LoginActivityModule {
    private LoginActivity loginActivity;

    public LoginActivityModule(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Provides
    @ActivityScope
    LoginActivity provideLoginActivity() {
        return loginActivity;
    }

    @Provides
    @ActivityScope
    LoginPresenter
    provideLoginPresenter(UserManager userManager) {
        return new LoginPresenter(userManager);
    }
}
