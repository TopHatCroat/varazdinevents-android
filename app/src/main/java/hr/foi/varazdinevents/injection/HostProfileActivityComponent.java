package hr.foi.varazdinevents.injection;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.HostProfileActivityModule;
import hr.foi.varazdinevents.places.facebook.FacebookActivity;
import hr.foi.varazdinevents.places.hostProfile.HostProfileActivity;

/**
 * Created by Valentin Magdić on 26.12.16..
 */

@Subcomponent(modules = {
        HostProfileActivityModule.class})
@ActivityScope
public interface HostProfileActivityComponent {
    HostProfileActivity inject(HostProfileActivity hostProfileActivity);
}

