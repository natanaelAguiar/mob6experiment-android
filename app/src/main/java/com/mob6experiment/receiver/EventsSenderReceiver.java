package com.mob6experiment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mob6experiment.event.EventDispatcher;
import com.mob6experiment.model.Event;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

public class EventsSenderReceiver extends BroadcastReceiver {

    private final EventDispatcher eventDispatcher;

    public EventsSenderReceiver(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null)
            return;

        Event event = new Event();
        event.setAction(intent.getAction());
        event.setWhen(DateTime.now().toString(ISODateTimeFormat.dateHourMinuteSecondMillis().withZoneUTC()));

        eventDispatcher.dispatch(event);
    }

}
