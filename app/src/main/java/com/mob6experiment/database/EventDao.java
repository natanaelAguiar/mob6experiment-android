package com.mob6experiment.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mob6experiment.model.Event;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> list();

    @Insert
    void insert(Event event);

    @Delete
    void delete(List<Event> events);

}
