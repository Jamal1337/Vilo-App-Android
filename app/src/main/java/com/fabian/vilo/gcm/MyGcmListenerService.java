package com.fabian.vilo.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fabian.vilo.R;
import com.fabian.vilo.Tabbar;
import com.fabian.vilo.me_screen.Me;
import com.google.android.gms.gcm.GcmListenerService;

import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Fabian on 23/11/15.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String title = data.getString("title");
        Integer badgeCount = Integer.parseInt(data.getString("badge"));

        //ShortcutBadger.with(getApplicationContext()).count(badgeCount);

        try {
            ShortcutBadger.setBadge(getApplicationContext(), badgeCount);
        } catch (ShortcutBadgeException e) {
            //handle the Exception
        }

        Intent notificationIntent = new Intent(getApplicationContext(), Me.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Tabbar.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT); //PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.v)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(intent);



        final int notificationId = 1;
        NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(notificationId, mBuilder.build());


    }
}