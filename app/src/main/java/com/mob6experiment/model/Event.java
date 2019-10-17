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

    @ColumnInfo(name = "screenOn")
    private Boolean screenOn;

    @ColumnInfo(name = "isInteractive")
    private Boolean isInteractive;

    @ColumnInfo(name = "isPowerSaveMode")
    private Boolean isPowerSaveMode;

    @ColumnInfo(name = "isIdleMode")
    private Boolean isIdleMode;

    @ColumnInfo(name = "batteryPercentage")
    private Integer batteryPercentage;

    @ColumnInfo(name = "isCharging")
    private Boolean isCharging;

    @ColumnInfo(name = "isUsbCharge")
    private Boolean isUsbCharge;

    @ColumnInfo(name = "isAcCharge")
    private Boolean isAcCharge;

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

    public Boolean getScreenOn() {
        return screenOn;
    }

    public void setScreenOn(Boolean screenOn) {
        this.screenOn = screenOn;
    }

    public Boolean getInteractive() {
        return isInteractive;
    }

    public void setInteractive(Boolean interactive) {
        isInteractive = interactive;
    }

    public Boolean getPowerSaveMode() {
        return isPowerSaveMode;
    }

    public void setPowerSaveMode(Boolean powerSaveMode) {
        isPowerSaveMode = powerSaveMode;
    }

    public Boolean getIdleMode() {
        return isIdleMode;
    }

    public void setIdleMode(Boolean idleMode) {
        isIdleMode = idleMode;
    }

    public Integer getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(Integer batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public Boolean getCharging() {
        return isCharging;
    }

    public void setCharging(Boolean charging) {
        isCharging = charging;
    }

    public Boolean getUsbCharge() {
        return isUsbCharge;
    }

    public void setUsbCharge(Boolean usbCharge) {
        isUsbCharge = usbCharge;
    }

    public Boolean getAcCharge() {
        return isAcCharge;
    }

    public void setAcCharge(Boolean acCharge) {
        isAcCharge = acCharge;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }
}
