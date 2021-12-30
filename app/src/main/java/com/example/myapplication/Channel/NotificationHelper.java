package com.example.myapplication.Channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.myapplication.R;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.ref.PhantomReference;

public class NotificationHelper extends ContextWrapper {
    private static final String ChennelId = "com.example.myapplication";
    private static final String Chennel_Nmae = "fatari";
    private NotificationManager manager;


    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(ChennelId, Chennel_Nmae, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager().createNotificationChannel(notificationChannel);

    }

    public NotificationManager notificationManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }


    public Notification.Builder getNotification(String title, String body, PendingIntent intent ,Uri soundUri) {
        return new Notification.Builder(getApplicationContext(), ChennelId).setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.car)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));
    }
    public Notification.Builder getNotificationActions(String title, String body, Uri soundUri, Notification.Action acetpAction) {
        return new Notification.Builder(getApplicationContext(), ChennelId).setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .addAction(acetpAction)
                .setSmallIcon(R.drawable.car)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

}
