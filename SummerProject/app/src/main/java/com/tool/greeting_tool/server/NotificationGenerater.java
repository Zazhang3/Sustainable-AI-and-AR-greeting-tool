package com.tool.greeting_tool.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.tool.greeting_tool.R;
import com.tool.greeting_tool.ui.home.HomeFragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Init and send a notification
 * Cancel the notificatiion created
 */
public class NotificationGenerater {
    private static final String CHANNEL_ID = "ECardNotification";
    private static final String CHANNEL_NAME = "Notification";
    private static final int NOTIFiCATION_ID = 1;
    private final NotificationManager notificationManager;
    private final Context context;
    private final String postcode;
    private final Timer timer;

    /**
     * Init NotificationGenerater with Context(An activity) and notificationImportance
     * @param context
     * @param notificationImportance
     * @param postcode
     */
    public NotificationGenerater(Context context, int notificationImportance, String postcode, Timer timer) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationImportance);
        this.postcode = postcode;
        this.timer = timer;
        startTimer();
    }
    /**Initialize a new NotificationChannel Class
     * NotificationImportance is needed to init this class
     * @param notificationImportance
     */
    private void createNotificationChannel(int notificationImportance) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, notificationImportance);
        notificationManager.createNotificationChannel(channel);
    }
    /**Set a timer to send notification when users receive message in postcode area
     */
    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int msgCount = getMsgCountFromServer(postcode);
                String notificationTitle = "Notification";
                if (msgCount == 1) {
                    String notificationContent = "You have a new message!";
                    sendNotification(notificationTitle, notificationContent, HomeFragment.class);
                } else if (msgCount > 1) {
                    String notificationContent = "You have " + msgCount + " messages!";
                    sendNotification(notificationTitle, notificationContent, HomeFragment.class);
                }
            }
        }, 0, 60000); // Check per 1 min
    }
    /**Get the count of msg received in current postcode area
     * @param postcode
     * @return
     */
    private int getMsgCountFromServer(String postcode) {
        // Implement count of Msg received in a postcode area
        // TODO
        // tmp part
        int msgCount = 0;
        return msgCount;
    }
    /**Init and send the notification
     * @param title
     * @param message
     * @param targetActivity
     */
    public void sendNotification(String title, String message, Class<?> targetActivity) {
        // Init a PendingIntent Class which decide jump to which activity
        PendingIntent pendingIntent = createPendingIntent(targetActivity);
        // Init a new Notification Class
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title) // Notification title
                .setContentText(message) // Notification message
                .setSmallIcon(R.drawable.ic_home_black_24dp) // Notification SmallIcon picture
                .setContentIntent(pendingIntent) // Jump to where after click
                .setAutoCancel(true) // Auto cancel notification on
                .build();
        // Send the notification
        notificationManager.notify(NOTIFiCATION_ID, notification);
    }
    /**Get a PendingIntent to jump to an activity
     * @param targetActivity
     * @return pendingIntent
     */
    private PendingIntent createPendingIntent(Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return pendingIntent;
    }
    /**Cancel notification according to notificationID
     * @param notificationId
     */
    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }
}
