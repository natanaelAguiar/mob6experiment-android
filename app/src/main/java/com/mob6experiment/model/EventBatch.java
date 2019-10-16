package com.mob6experiment.model;

import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;

import java.util.List;

public class EventBatch {

    private static Long SEQ = System.currentTimeMillis();

    private Long seq;
    private String manufacturer;
    private String model;
    private Integer sdkVersion;
    private String androidId;
    private String ipAddress;
    private String macAddress;
    private String when;
    private List<Event> events;

    public EventBatch(Context context, List<Event> events) {
        this.seq = SEQ++;
        this.manufacturer = Build.MANUFACTURER;
        this.model = Build.MODEL;
        this.sdkVersion = Build.VERSION.SDK_INT;
        this.androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        this.events = events;
    }

    public Long getSeq() {
        return seq;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public Integer getSdkVersion() {
        return sdkVersion;
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public List<Event> getEvents() {
        return events;
    }

}
