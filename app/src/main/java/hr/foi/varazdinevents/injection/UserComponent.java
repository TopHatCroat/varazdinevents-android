package hr.foi.varazdinevents.injection;

import android.support.annotation.NonNull;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.injection.modules.UserModule;

/**
 * Created by Antonio Martinović on 09.11.16.
 */

@UserScope
@Subcomponent(
        modules = {
                UserModule.class
        }
)
public interface UserComponent {

    @NonNull
    MainActivityComponent plus(@NonNull MainActivityModule mainActivityModule);
    @NonNull
    EventDetailsActivityComponent plus(@NonNull EventDetailsActivityModule eventDetailsActivityModule);
}
