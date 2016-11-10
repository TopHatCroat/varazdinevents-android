package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.LoginActivityModule;
import hr.foi.varazdinevents.places.login.LoginActivity;

/**
 * Created by Antonio Martinović on 09.11.16.
 */
@ActivityScope
@Subcomponent(
        modules = LoginActivityModule.class
)
public interface LoginActivityComponent {
    LoginActivity inject(LoginActivity loginActivity);
}
