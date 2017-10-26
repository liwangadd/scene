package com.windylee.scene.utils;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.WindowManager;

import java.io.DataOutputStream;
import java.lang.reflect.Method;

/**
 * Created by windylee on 4/18/17.
 */

public class MobileSettingManager {

    private static final int MIN_BRIGHTNESS = 30;
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * 判断手机蓝牙是否打开
     *
     * @return 手机蓝牙状态
     */
    public static boolean getBluetoothState() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    /**
     * 设置手机蓝牙状态
     *
     * @param state
     */
    public static void setBluetoothState(boolean state) {
        if (state) BluetoothAdapter.getDefaultAdapter().enable();
        else BluetoothAdapter.getDefaultAdapter().disable();
    }

    /**
     * 判断手机是否打开了流量开关
     *
     * @param context
     * @return
     */
    public static boolean getGPRSState(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = conMgr.getClass();
        Class[] argClasses = null;
        Object[] argObjects = null;
        boolean isOpen = false;
        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);
            isOpen = (boolean) method.invoke(conMgr, argObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    /**
     * 设置流量开关是否打开
     *
     * @param context
     * @param state
     */
    public static void setGPRSState(Context context, boolean state) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = conMgr.getClass();
        Class[] argClasses = new Class[1];
        argClasses[0] = boolean.class;
        try {
            Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);
            method.invoke(conMgr, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRingState(Context context, int status) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        audioManager.setRingerMode(status);
    }

    public static int getRingState(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    public static void setRingMusic(Context context) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        //允许用户检测到默认值
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        //显示铃声类型
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        //设置通知音量
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        //设置是否允许静音
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        // Otherwise pick default ringtone Uri so that something is selected.
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Put checkmark next to the current ringtone for this contact
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, notificationUri);
        context.startActivity(intent);
    }

    /**
     * 获取屏幕亮度调节方式
     *
     * @param context
     * @return
     */
    public static int getScreenLightMode(Context context) {
        int mode = -1;
        try {
            mode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode;
    }

    /**
     * 获取屏幕亮度
     *
     * @param context
     * @return
     */
    public static int getScreenLight(Context context) {
        int lightness = -1;
        try {
            lightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return lightness;
    }

    /**
     * 设置屏幕亮度调节方式
     *
     * @param context
     * @param mode
     */
    public static void setScreenLightMode(Context context, int mode) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
    }

    /**
     * 设置屏幕亮度
     *
     * @param context
     * @param lightness
     */
    public static void setScreenLight(Context context, int lightness) {
        lightness = lightness < MIN_BRIGHTNESS ? MIN_BRIGHTNESS : lightness;
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, lightness);
    }

    /**
     * 动态调节屏幕亮度
     *
     * @param context
     * @param lightness
     */
    public static void showScreenLight(Activity context, int lightness) {
        lightness = lightness < MIN_BRIGHTNESS ? MIN_BRIGHTNESS : lightness;
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = lightness / (float) (MAX_BRIGHTNESS);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 设置屏幕方向是否锁定
     *
     * @param context
     * @param state
     */
    public static void setScreenRotate(Context context, int state) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, state);
    }

    /**
     * 获取屏幕方向是否锁定
     *
     * @param context
     * @return false表示未锁定，true表示已锁定
     */
    public static boolean getScreenRotate(Context context) {
        int mode = -1;
        try {
            mode = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (mode == 1) return false;
        return true;
    }

    /**
     * 设置wifi是否打开
     *
     * @param context
     * @param state
     */
    public static void setWiFiState(Context context, boolean state) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);
    }

    /**
     * 获取WiFi状态
     *
     * @param context
     * @return false表示WiFi关闭，true表示WiFi打开
     */
    public static boolean getWiFiState(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if (state == 1) return false;
        return true;
    }

    /**
     * 打开WiFi热点
     *
     * @param mSSID   热点名称
     * @param mPasswd 热点密码
     */
    public static void startWiFiAp(Context context, String mSSID, String mPasswd) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;

            netConfig.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);

            method.invoke(wifiManager, netConfig, true);

        } catch (Exception e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
    }

    public static void closeWiFiAp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
        if (isWifiApEnabled(wifiManager)) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);

                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);

                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled",
                        WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isWifiApEnabled(WifiManager wifiManager) {
        boolean flag = false;
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            flag = (boolean) method.invoke(wifiManager);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    public static String getWiFiName(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Service.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }
}
