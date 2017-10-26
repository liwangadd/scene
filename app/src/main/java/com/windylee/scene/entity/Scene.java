package com.windylee.scene.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by windy on 17/4/19.
 */

@Table("scene")
public class Scene {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;
    @Column("scene_name")
    private String sceneName;
    @Column("scene_desc")
    private String sceneDesc;
    @Column("alarm_v")
    private int alarmVolume;
    @Column("media_v")
    private int mediaVolume;
    @Column("ring_v")
    private int ringVolume;
    @Column("gprs")
    private int gprs;
    @Column("wifi")
    private int wifi;
    @Column("wifi_name")
    private String wifiname;
    @Column("ring_mode")
    private int ringmode;
    @Column("light_mode")
    private int lightmode;
    @Column("lightness")
    private int lightness;
    @Column("bluetooth")
    private int bluetooth;
    @Column("latitude")
    private double latitude;
    @Column("longitude")
    private double longitude;
    @Column("hour")
    private int hour;
    @Column("minute")
    private int minute;
    @Column("auto_rotate")
    private int autoRotate;
    @Column("ringUri")
    private String ringUri;
    @Column("type")
    private int type;
    @Column("is_open")
    @Default("false")
    private boolean isOpen;
    @Ignore
    private int userId;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneDesc() {
        return sceneDesc;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public int getAlarmVolume() {
        return alarmVolume;
    }

    public void setAlarmVolume(int alarmVolume) {
        this.alarmVolume = alarmVolume;
    }

    public int getMediaVolume() {
        return mediaVolume;
    }

    public void setMediaVolume(int mediaVolume) {
        this.mediaVolume = mediaVolume;
    }

    public int getRingVolume() {
        return ringVolume;
    }

    public void setRingVolume(int ringVolume) {
        this.ringVolume = ringVolume;
    }

    public int getGprs() {
        return gprs;
    }

    public void setGprs(int gprs) {
        this.gprs = gprs;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }

    public int getRingmode() {
        return ringmode;
    }

    public void setRingmode(int ringmode) {
        this.ringmode = ringmode;
    }

    public int getLightmode() {
        return lightmode;
    }

    public void setLightmode(int lightmode) {
        this.lightmode = lightmode;
    }

    public int getLightness() {
        return lightness;
    }

    public void setLightness(int lightness) {
        this.lightness = lightness;
    }

    public int getBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(int bluetooth) {
        this.bluetooth = bluetooth;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getAutoRotate() {
        return autoRotate;
    }

    public void setAutoRotate(int autoRotate) {
        this.autoRotate = autoRotate;
    }

    public String getRingUri() {
        return ringUri;
    }

    public void setRingUri(String ringUri) {
        this.ringUri = ringUri;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "id=" + id +
                ", sceneName='" + sceneName + '\'' +
                ", sceneDesc='" + sceneDesc + '\'' +
                ", alarmVolume=" + alarmVolume +
                ", mediaVolume=" + mediaVolume +
                ", ringVolume=" + ringVolume +
                ", gprs=" + gprs +
                ", wifi=" + wifi +
                ", wifiname='" + wifiname + '\'' +
                ", ringmode=" + ringmode +
                ", lightmode=" + lightmode +
                ", lightness=" + lightness +
                ", bluetooth=" + bluetooth +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", hour=" + hour +
                ", minute=" + minute +
                ", autoRotate=" + autoRotate +
                ", ringUri='" + ringUri + '\'' +
                ", type=" + type +
                ", isOpen=" + isOpen +
                '}';
    }
}
