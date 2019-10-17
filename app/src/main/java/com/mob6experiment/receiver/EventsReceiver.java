package com.mob6experiment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;

import com.mob6experiment.App;
import com.mob6experiment.event.EventDispatcher;
import com.mob6experiment.model.Event;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import static android.content.Intent.ACTION_BATTERY_LOW;
import static android.content.Intent.ACTION_BATTERY_OKAY;
import static android.content.Intent.ACTION_POWER_CONNECTED;
import static android.content.Intent.ACTION_POWER_DISCONNECTED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static android.os.PowerManager.ACTION_POWER_SAVE_MODE_CHANGED;


public class EventsReceiver extends BroadcastReceiver {

    private final EventDispatcher eventDispatcher;

    public EventsReceiver(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (action == null)
                return;

            Event event = new Event();
            event.setAction(action.substring(action.lastIndexOf(".") + 1));
            event.setWhen(DateTime.now().toString(ISODateTimeFormat.dateHourMinuteSecondMillis().withZoneUTC()));

            fetchPowerManagerStatus(context, event);
            fetchDisplayManagerStatus(context, event);
            fetchBatteryStatus(context, event);

            eventDispatcher.dispatch(event);
        } catch (Exception e) {
            Log.e(App.TAG, "Failed to receive and process action", e);
        }
    }

    private void fetchPowerManagerStatus(Context context, Event event) {
        PowerManager powerManager = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            event.setInteractive(powerManager.isInteractive());
            event.setPowerSaveMode(powerManager.isPowerSaveMode());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                event.setIdleMode(powerManager.isDeviceIdleMode());
            }
        } else {
            Log.w(App.TAG, "Could not fetch the power manager");
        }
    }

    private void fetchDisplayManagerStatus(Context context, Event event) {
        DisplayManager displayManager = (DisplayManager) context.getApplicationContext().getSystemService(Context.DISPLAY_SERVICE);
        if (displayManager != null) {
            Display display = displayManager.getDisplay(Display.DEFAULT_DISPLAY);
            event.setScreenOn(display.getState() == Display.STATE_ON);
        } else {
            Log.w(App.TAG, "Could not fetch the display manager");
        }
    }

    private void fetchBatteryStatus(Context context, Event event) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        if (batteryStatus == null) {
            Log.w(App.TAG, "Could not fetch the battery status");
            return;
        }

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        event.setCharging(status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL);

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        event.setUsbCharge(chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
        event.setAcCharge(chargePlug == BatteryManager.BATTERY_PLUGGED_AC);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        event.setBatteryPercentage((int) (100 * level / (float) scale));
    }

    public static String[] getActionsToListen() {
        return new String[]{
                ACTION_SCREEN_ON,
                ACTION_SCREEN_OFF,
                ACTION_POWER_CONNECTED,
                ACTION_POWER_DISCONNECTED,
                ACTION_BATTERY_LOW,
                ACTION_BATTERY_OKAY,
                ACTION_POWER_SAVE_MODE_CHANGED
        };
    }

}
