package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.AboutModule;
import hr.foi.varazdinevents.places.about.AboutActivity;

/**
 * Created by Antonio Martinović on 22.01.17.
 */
@Subcomponent(modules = {
        AboutModule.class})
@ActivityScope
public interface AboutActivityComponent {
    AboutActivity inject(AboutActivity aboutActivity);
}
