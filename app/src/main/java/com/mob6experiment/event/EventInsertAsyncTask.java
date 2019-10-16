package com.mob6experiment.event;

import android.os.AsyncTask;
import android.util.Log;

import com.mob6experiment.App;
import com.mob6experiment.database.EventDao;
import com.mob6experiment.model.Event;

class EventInsertAsyncTask extends AsyncTask<Event, Void, Void> {

    private final EventDao eventDao;

    EventInsertAsyncTask(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    protected Void doInBackground(Event... events) {
        try {
            for (Event event : events) {
                eventDao.insert(event);
            }
        } catch (Exception e) {
            Log.e(App.TAG, "Failed to insert an event into database", e);
        }

        return null;
    }

}
