package com.mob6experiment;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.bugfender.sdk.Bugfender;
import com.mob6experiment.database.AppDatabase;


public class App extends Application {

    public static final String TAG = "Mob6Experiment";
    public static final String SERVER_IP = "fe80::cfff";
    private static final String BUGFENDER_APP_TOKEN = "GDG9MtHhXuMeUMI20GvaE41uNIM7NVPm";
    public static final Integer SERVER_PORT = 9099;
    public static final Integer CONNECTION_TIMEOUT = 5000;
    public static final Integer SEND_INTERVAL = 60_000;
    private static AppDatabase APP_DATABASE;

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (APP_DATABASE == null) {
            APP_DATABASE = Room.databaseBuilder(
                    context.getApplicationContext(), AppDatabase.class, TAG).build();
        }

        return APP_DATABASE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Bugfender.init(this, BUGFENDER_APP_TOKEN, BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();
        Bugfender.enableLogcatLogging();
    }
}
