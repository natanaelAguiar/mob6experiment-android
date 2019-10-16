package com.mob6experiment;

import android.content.Context;

import androidx.room.Room;

import com.mob6experiment.database.AppDatabase;

import static android.content.Intent.ACTION_POWER_CONNECTED;
import static android.content.Intent.ACTION_POWER_DISCONNECTED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

public class App {

    public static final String TAG = "Mob6Experiment";
    public static final String[] INTENTS_TO_TRACK = {
            ACTION_SCREEN_ON,
            ACTION_SCREEN_OFF,
            ACTION_POWER_CONNECTED,
            ACTION_POWER_DISCONNECTED
    };
    public static final String SERVER_IP = "fe80::cfff";
    public static final Integer SERVER_PORT = 9099;
    public static final Integer CONNECTION_TIMEOUT = 5000;
    public static final Integer SEND_INTERVAL = 5000;
    private static AppDatabase APP_DATABASE;

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (APP_DATABASE == null) {
            APP_DATABASE = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, TAG).build();
        }

        return APP_DATABASE;
    }

}
