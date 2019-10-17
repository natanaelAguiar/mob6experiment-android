package com.mob6experiment.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.mob6experiment.App;
import com.mob6experiment.R;
import com.mob6experiment.event.EventDispatcher;
import com.mob6experiment.receiver.EventsReceiver;

public class EventsService extends Service {

    private EventsReceiver eventsReceiver;
    private EventDispatcher eventDispatcher;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(100);
        for (String intentToTrack : EventsReceiver.getActionsToListen()) {
            intentFilter.addAction(intentToTrack);
        }

        eventDispatcher = new EventDispatcher(this);
        eventsReceiver = new EventsReceiver(eventDispatcher);

        registerReceiver(eventsReceiver, intentFilter);
        Log.d(App.TAG, "The receiver has been registered");

        startForegroundService();
        Log.d(App.TAG, "The service has been promoted to foreground");

        eventDispatcher.start();
        Log.d(App.TAG, "The eventDispatcher has been started");
    }

    private void startForegroundService() {
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String notificationChannel = createNotificationChannel(getString(R.string.channel_id),
                    "EventsBroadcastReceiver");

            notification = new Notification.Builder(this, notificationChannel)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notification))
                    .setSmallIcon(R.drawable.icon)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notification))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(R.drawable.icon)
                    .build();
        }

        startForeground(101, notification);
    }

    private String createNotificationChannel(String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        service.createNotificationChannel(chan);
        return channelId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.w(App.TAG, "The service has been destroyed. Stopping receiver and dispatcher...");
        unregisterReceiver(eventsReceiver);
        eventDispatcher.stop();
    }
}
