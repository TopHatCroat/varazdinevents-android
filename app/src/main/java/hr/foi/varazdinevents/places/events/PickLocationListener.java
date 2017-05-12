package hr.foi.varazdinevents.places.events;

import android.location.Location;

/**
 * Created by Antonio Martinović on 10/05/17.
 */

public interface PickLocationListener {
    void setLocation(Location location);
    void setManual();

    void requestPermission();
}
