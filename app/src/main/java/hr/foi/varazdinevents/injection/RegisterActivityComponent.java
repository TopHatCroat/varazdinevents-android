package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.LoginActivityModule;
import hr.foi.varazdinevents.injection.modules.RegisterActivityModule;
import hr.foi.varazdinevents.places.login.LoginActivity;
import hr.foi.varazdinevents.places.register.RegisterActivity;

/**
 * Created by Bruno on 09.11.16.
 */
@ActivityScope
@Subcomponent(
        modules = RegisterActivityModule.class
)
public interface RegisterActivityComponent {
    RegisterActivity inject(RegisterActivity registerActivity);
}
