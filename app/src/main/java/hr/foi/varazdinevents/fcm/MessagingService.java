package hr.foi.varazdinevents.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hr.foi.varazdinevents.places.eventDetails.EventDetailsActivity;

/**
 * Handles receiving push notifications by the Firebase API
 */
public class MessagingService extends FirebaseMessagingService {
    public static final String TAG = "FIREBASE_MESSAGE";
    public static boolean allowNotifications = true;

    public MessagingService() {
    }

    /**
     * Creates a simple notification when a push notification is received
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        if (allowNotifications) {
            PendingIntent contentIntent =
                    PendingIntent.getActivity(this, 0, new Intent(this, EventDetailsActivity.class), 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody());

            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(666, mBuilder.build());
        }
    }
}
