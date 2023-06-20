package com;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.uptree.lawdiary.Activities.Dashboard;
import com.uptree.lawdiary.Activities.TodayCasesActivity;
import com.uptree.lawdiary.R;

public class NotiiService extends IntentService {


    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private static int NOTIFICATION_ID = 1;
    Notification notification;


    String channelId = "channel-01";
    String channelName = "Channel Name";
    int importance = NotificationManager.IMPORTANCE_HIGH;


    public NotiiService() {
        super("NotiiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = this.getApplicationContext();
        //showNotification(context);
        pushNotification(context);


        notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, TodayCasesActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("test", "test");
        mIntent.putExtras(bundle);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notification = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setTicker("ticker value")
                .setAutoCancel(true)
                .setPriority(8)
                .setSound(soundUri)
                .setContentTitle("Notif title")
                .setContentText("Text").build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = 0xFFFFA500;
        notification.ledOnMS = 800;
        notification.ledOffMS = 1000;
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
        Log.i("notif","Notifications sent.");


    }

    private void pushNotification(Context context) {
        Log.d("push", "pushNotification: ");
        int returnFlag = 0;
        int NOTIFICATION_ID = 1234;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationCh();
            Notification.Builder builder = new Notification.Builder(this, channelId)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.logo)
                    .setChannelId(channelId)
                    .setContentTitle("Today cases").setContentText("Touch to view the detail");
            Intent notificationIntent = new Intent(NotiiService.this, TodayCasesActivity.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            Notification notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);
            Log.i("notifi","Notifications sent.");

            returnFlag = START_NOT_STICKY;
        }else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setChannelId(channelId)
                    .setContentTitle("Today cases").setContentText("Touch to View the Detail")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Intent notificationIntent = new Intent(NotiiService.this, TodayCasesActivity.class);
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);

            returnFlag = START_STICKY;
        }
        //mHandlerTask.run();
        //return returnFlag;

    /*private void showNotification(Context context) {
        Log.d("noti", "showNotification: ");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, TodayCasesActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Today case")
                        .setContentTitle("casetitle")
                        .setContentText("casecategory");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }*/
    }

    private void createNotificationCh() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(channelId,
                    channelName,
                    importance
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

    }
}

   