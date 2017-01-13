package hr.foi.varazdinevents.injection;

import android.support.annotation.NonNull;

import dagger.Subcomponent;
import hr.foi.varazdinevents.injection.modules.EventDetailsActivityModule;
import hr.foi.varazdinevents.injection.modules.HostProfileActivityModule;
import hr.foi.varazdinevents.injection.modules.MainActivityModule;
import hr.foi.varazdinevents.injection.modules.NewEventModule;
import hr.foi.varazdinevents.injection.modules.SettingsActivityModule;
import hr.foi.varazdinevents.injection.modules.UserModule;

/**
 * Component class specifying the exact subcomponent that will have
 * access objects provided by {@link UserModule};
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
    @NonNull
    SettingsActivityComponent plus(@NonNull SettingsActivityModule settingsActivityModule);
    @NonNull
    NewEventComponent plus(@NonNull NewEventModule newEventModule);
    @NonNull
    HostProfileActivityComponent plus(@NonNull HostProfileActivityModule hostProfileActivityModule);
}
