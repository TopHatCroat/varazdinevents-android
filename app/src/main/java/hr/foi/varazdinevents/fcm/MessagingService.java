package hr.foi.varazdinevents.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import hr.foi.varazdinevents.models.Event;
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
        if (allowNotifications) {
            Intent notificationIntent = new Intent(getApplicationContext(), EventDetailsActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            Event event = new Event();
            event.setApiId(0);

            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                    notificationIntent, 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody())
                    .setContentIntent(intent);


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            notificationManager.notify(666, mBuilder.build());
        }
    }
}
