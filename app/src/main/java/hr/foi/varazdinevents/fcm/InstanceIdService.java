package hr.foi.varazdinevents.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by Antonio Martinović on 23.12.16.
 */

public class InstanceIdService extends FirebaseInstanceIdService {
    public static String TAG = "FIREBASE_SERVICE";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
    }

}
