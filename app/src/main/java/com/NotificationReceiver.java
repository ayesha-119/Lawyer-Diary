package com;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.uptree.lawdiary.Activities.Dashboard;
import com.uptree.lawdiary.R;

public class NotificationReceiver extends BroadcastReceiver {
    String  casetitle,casecategory;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("MyAPP", "onReceive called");

        Intent intent1 = new Intent(context, NotiiService.class);
        intent1.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(intent1);
    }

    private void showNotification(Context context) {

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Dashboard.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Today case")
                        .setContentTitle(casetitle)
                        .setContentText(casecategory);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}