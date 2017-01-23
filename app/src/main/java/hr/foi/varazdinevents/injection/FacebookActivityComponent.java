package hr.foi.varazdinevents.injection;


import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.FacebookActivityModule;
import hr.foi.varazdinevents.places.facebook.FacebookActivity;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

@Subcomponent(modules = {
        FacebookActivityModule.class})
@ActivityScope
public interface FacebookActivityComponent {
    FacebookActivity inject(FacebookActivity facebookActivity);
}

