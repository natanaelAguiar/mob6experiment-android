package com.mob6experiment.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mob6experiment.model.Event;

@Database(entities = {Event.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EventDao eventDao();

}
