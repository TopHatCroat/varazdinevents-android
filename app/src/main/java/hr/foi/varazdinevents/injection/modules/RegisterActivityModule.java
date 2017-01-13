package hr.foi.varazdinevents.injection.modules;

import dagger.Module;
import dagger.Provides;
import hr.foi.varazdinevents.api.UserManager;
import hr.foi.varazdinevents.injection.ActivityScope;
import hr.foi.varazdinevents.places.register.RegisterActivity;
import hr.foi.varazdinevents.places.register.RegisterPresenter;

/**
 * Created by Bruno on 09.11.16..
 */
@Module
public class RegisterActivityModule {
    private RegisterActivity registerActivity;

    public RegisterActivityModule(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    @Provides
    @ActivityScope
    RegisterActivity provideRegisterActivity() {
        return registerActivity;
    }

    @Provides
    @ActivityScope
    RegisterPresenter
    provideRegisterPresenter(UserManager userManager) {
        return new RegisterPresenter(userManager);
    }
}
