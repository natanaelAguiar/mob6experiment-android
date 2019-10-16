package com.mob6experiment.event;

import android.content.Context;
import android.util.Log;

import com.mob6experiment.App;
import com.mob6experiment.database.EventDao;
import com.mob6experiment.model.Event;
import com.mob6experiment.model.EventBatch;
import com.mob6experiment.network.NetworkInterfaceHelper;

import java.net.Inet6Address;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EventDispatcher {

    private final Context context;
    private final EventDao eventDao;
    private final Timer timer;
    private boolean started;

    public EventDispatcher(Context context) {
        this.context = context;
        this.timer = new Timer();
        this.started = false;
        this.eventDao = App.getAppDatabase(context).eventDao();
    }

    public void dispatch(Event event) {
        new EventInsertAsyncTask(eventDao).execute(event);
    }

    private void dispatch() throws Exception {
        List<Event> events = eventDao.list();
        if (events.isEmpty())
            return;

        EventBatch eventBatch = new EventBatch(context, events);
        List<Inet6Address> iPv6Addresses = NetworkInterfaceHelper.getIPv6FromWifiInterface(context);
        boolean wasSuccessfullySent = new EventBatchSender(iPv6Addresses, eventBatch).send();
        if (wasSuccessfullySent) {
            Log.i(App.TAG, String.format("Successfully dispatched an event batch with %d events", events.size()));
            eventDao.delete(events);
        } else {
            Log.w(App.TAG, "It wasn't possible to dispatch events, will try again soon");
        }
    }

    public void start() {
        synchronized (this) {
            if (!started) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            dispatch();
                        } catch (Exception e) {
                            Log.e(App.TAG, "Failed to dispatch event batch to server", e);
                        }
                    }
                }, 0, App.SEND_INTERVAL);
                started = true;
            }
        }
    }

    public void stop() {
        synchronized (this) {
            if (started) {
                timer.cancel();
                started = false;
            }
        }
    }

}
