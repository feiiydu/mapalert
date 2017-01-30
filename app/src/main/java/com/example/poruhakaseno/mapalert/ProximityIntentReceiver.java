package com.example.poruhakaseno.mapalert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.util.Log;


import static java.security.AccessController.getContext;

public class ProximityIntentReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1000;
    Notification notification ;
    Context context2 ;

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        context2 = context;
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),
                R.drawable.icongg);
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);
        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
        }else {
            Log.d(getClass().getSimpleName(), "exiting");
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, Map.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(true);
        builder.setTicker("Map Alert !!");
        builder.setContentTitle("Map Alert !!");
        builder.setContentText("You are near your destination...");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(bitmap);
        builder.setVibrate(new long[] { 500, 1000, 500 });
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.setSubText("Please looking for your destination.");
        builder.build();

        notification = builder.getNotification();
        notificationManager.notify(77, notification);
    }


}
