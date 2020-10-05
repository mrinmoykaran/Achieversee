package com.example.edukit;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.edukit.App.CHANNEL_1_ID;

public class FCMMessageReceiverService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("Mrinmoy", "onMessageReceived()");
        Log.d("Mrinmoy", "onMessageReceived():Message Received from" + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_open_book)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.GREEN)
                    .build();
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify((int) System.currentTimeMillis(), notification);

        }
        if (remoteMessage.getData().size() > 0) {
            Log.d("Mrinmoy", "onMessageReceived():Data Received from" + remoteMessage.getData().toString());


        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d("Mrinmoy", "onDeletedMessage()");
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("Mrinmoy", "onNewToken()");
        //Upload this token on application server

    }

}
