package com.windylee.scene.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by windylee on 4/18/17.
 */

public class VolumeManager {

    private Context context;
    private AudioManager audioManager;

    public VolumeManager(Context context){
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
    }
    //设置闹钟音量
    public void setAlarmVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,
                volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    //设置音乐音量
    public void setMusicVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    //设置提示音音量
    public void setNotifactionVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
                volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    //设置响铃音量
    public void setRingVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_RING,
                volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    //设置系统音量
    public void setSystemVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,
                volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    //设置语音电话的音量
    public void setCallVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    //获得闹钟音量
    public int getAlarmVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
    }
    //获得音乐音量
    public int getMusicVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    //获得提示音音量
    public int getNotifactionVolume(){

        return audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
    }
    //获得响铃音量
    public int getRingVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_RING);
    }
    //获得系统音量
    public int getSystemVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }
    //获得语音电话音量
    public int getCallVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    //获得最大闹钟音量
    public int getMaxAlarmVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }
    //获得最大音乐音量
    public int getMaxMusicVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
    //获得最大提示音音量
    public int getMaxNotificationVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
    }
    //获得最大响铃音量
    public int getMaxRingVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }
    //获得最大系统音量
    public int getMaxSystemVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
    }
    //获得最大语音电话音量
    public int getMaxCallVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
    }

}
