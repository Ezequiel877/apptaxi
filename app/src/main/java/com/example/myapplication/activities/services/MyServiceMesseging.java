package com.example.myapplication.activities.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.Channel.NotificationHelper;
import com.example.myapplication.R;
import com.example.myapplication.activities.reciber.AceptRessiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import retrofit2.http.Url;

public class MyServiceMesseging extends FirebaseMessagingService {
    private static final int NOTICATION_CODE = 100;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notif = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        String idCliente=data.get("idCliente");
       showNotificationAction(title, body, idCliente);
    }

    private void showNotification(String title, String body) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(), 0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        Uri url = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotification(title, body, intent, url);
        notificationHelper.notificationManager().notify(1, builder.build());
    }

    private void showNotificationAction(String title, String body, String idClient) {
        Intent showAction = new Intent(this, AceptRessiver.class);
        showAction.putExtra("idClien", idClient);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTICATION_CODE, showAction, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action action = new Notification.Action.Builder(
                R.mipmap.ic_launcher,
                "Aceptar",
                pendingIntent
        ).build();
        Uri url = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotificationActions(title, body, url, action);
        notificationHelper.notificationManager().notify(2, builder.build());
    }
}
