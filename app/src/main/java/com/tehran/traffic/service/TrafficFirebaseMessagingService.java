package com.tehran.traffic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mohsenoid.tehran.traffic.R;
import com.tehran.traffic.ui.MainActivity;

/**
 * Created by Mohsen on 04/12/2016.
 */

public class TrafficFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FirebaseMessaging";
    public static final String ALERT = "alert";
    public static final String MESSAGE = "msg";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String DEFAULT_TITLE = "Traffic";
    private static final String KEY_LAST_NOTIFICATION_TITLE = "last_notification";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (null == remoteMessage.getNotification()) {
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().get("message"));
            showNotification(remoteMessage.getMessageId().hashCode(), remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("url"));
        } else {
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            showNotification(remoteMessage.getMessageId().hashCode(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("url"));
        }
    }

    private void showNotification(int notificationId, String title, String message, String url) {
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (null == title || TextUtils.isEmpty(title))
            title = DEFAULT_TITLE;

        Intent intent;
        if (null == url || TextUtils.isEmpty(url)) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(ALERT, "1");
            intent.putExtra(MESSAGE, message);
            intent.putExtra(TITLE, title);
            intent.putExtra(URL, url);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews bigView = new RemoteViews(getPackageName(), R.layout.notif_big);
        bigView.setTextViewText(R.id.title, title);
        bigView.setTextViewText(R.id.text, message);

        RemoteViews smallView = new RemoteViews(getPackageName(), R.layout.notif_small);
        smallView.setTextViewText(R.id.title, title);

//                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Constructs the Builder object.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title + "\n" + message))
//                      .addAction (R.drawable.angry, getString(R.string.dismiss), contentIntent)
//                      .addAction (R.drawable.happy, getString(R.string.snooze), contentIntent)
//                      .setSound(uri)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setContent(smallView)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            notification.bigContentView = bigView;


        notificationManager.notify(notificationId, notification);
    }
}
