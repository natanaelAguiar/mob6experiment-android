package com.mob6experiment.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {

    @PrimaryKey(autoGenerate = true)
    private Integer seq;

    @ColumnInfo(name = "action")
    private String action;

    @ColumnInfo(name = "when")
    private String when;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

}
