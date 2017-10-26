package com.windylee.scene.service;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.orhanobut.logger.Logger;
import com.windylee.scene.application.APP;
import com.windylee.scene.entity.ConnEvent;
import com.windylee.scene.entity.Constants;
import com.windylee.scene.entity.Scene;
import com.windylee.scene.utils.MobileSettingManager;
import com.windylee.scene.utils.VolumeManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by windylee on 4/20/17.
 */

public class ApplySettingService extends Service implements AMapLocationListener {

    private Context mContext;
    private Map<Integer, List<Scene>> mScenes;
    private IntentFilter mIntentFilter;
    private WiFiReceiver mWiFiReceiver;
    private LocateService mLocateService;
    private VolumeManager mVolumeManager;
    private static final Object timeMinotor = new Object();
    private static final Object locateMinotor = new Object();
    private static final Object wlanMinotor = new Object();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public ApplySettingService(String name) {
//        super(name);
//        EventBus.getDefault().register(this);
//    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(ConnEvent event) {
        Logger.d(event);
        if (event.getType() == Constants.NORMAL_CODE) {
            applySetting(event.getScene());
        } else {
            if (event.getType() == Constants.TIME_CODE) {
                synchronized (timeMinotor) {
                    if (event.isOpen()) mScenes.get(event.getType()).add(event.getScene());
                    else mScenes.get(event.getType()).remove(event.getScene());
                }
            } else if (event.getType() == Constants.LOCATE_CODE) {
                synchronized (locateMinotor) {
                    if (event.isOpen()) mScenes.get(event.getType()).add(event.getScene());
                    else mScenes.get(event.getType()).remove(event.getScene());
                }
            } else {
                synchronized (wlanMinotor) {
                    if (event.isOpen()) mScenes.get(event.getType()).add(event.getScene());
                    else mScenes.get(event.getType()).remove(event.getScene());
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        EventBus.getDefault().register(this);
        mVolumeManager = new VolumeManager(mContext);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        mScenes = new HashMap<>();
        initScenes(Constants.WLAN_CODE);
        initScenes(Constants.LOCATE_CODE);
        initScenes(Constants.TIME_CODE);

        mWiFiReceiver = new WiFiReceiver();
        mContext.registerReceiver(mWiFiReceiver, mIntentFilter);

        mLocateService = new LocateService(mContext);
        mLocateService.requestSeriesLocation(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Calendar calendar = Calendar.getInstance();
                    synchronized (timeMinotor) {
                        Logger.d("it is this time, start");
                        List<Scene> timeScenes = mScenes.get(Constants.TIME_CODE);
                        for (Scene scene : timeScenes) {
                            if (calendar.get(Calendar.HOUR_OF_DAY) == scene.getHour() && calendar.get(Calendar.MINUTE) == scene.getMinute()) {
                                applySetting(scene);
                                break;
                            }
                        }
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void initScenes(int type) {
        mScenes.put(type, APP.mDb.query(
                new QueryBuilder<Scene>(Scene.class).where("type = ?", type)
                        .whereAppendAnd().where("is_open = ?", true)
        ));
    }

    private void applySetting(Scene mScene) {
        Logger.d(mScene);
        if (mScene.getBluetooth() != 0) {
            MobileSettingManager.setBluetoothState(mScene.getBluetooth() == 1);
        }
        if (mScene.getGprs() != 0) {
            MobileSettingManager.setGPRSState(mContext, mScene.getGprs() == 1);
        }
        if (mScene.getRingmode() != 0) {
            MobileSettingManager.setRingState(mContext, mScene.getRingmode() - 1);
        }
        if (mScene.getLightmode() != 0) {
            MobileSettingManager.setScreenLightMode(mContext, mScene.getLightmode() - 1);
        }
        MobileSettingManager.setScreenLight(mContext, mScene.getLightness());
        if (mScene.getAutoRotate() != 0) {
            MobileSettingManager.setScreenRotate(mContext, mScene.getAutoRotate() - 1);
        }
        if (mScene.getWifi() != 0) {
            MobileSettingManager.setWiFiState(mContext, mScene.getWifi() == 1);
        }
//        if (mScene.getWifiHot() == 1) {
//            MobileSettingManager.startWiFiAp(mContext, "hahahaha", "123456");
//        } else {
//            MobileSettingManager.closeWiFiAp(mContext);
//        }
        mVolumeManager.setAlarmVolume(mScene.getAlarmVolume());
        mVolumeManager.setMusicVolume(mScene.getMediaVolume());
        mVolumeManager.setRingVolume(mScene.getRingVolume());
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Logger.d("it is this place, start");
        double latitude = aMapLocation.getLatitude();
        double longtitude = aMapLocation.getLongitude();
        synchronized (locateMinotor) {
            List<Scene> locateScenes = mScenes.get(Constants.LOCATE_CODE);
            for (Scene scene : locateScenes) {
                if (Math.abs(latitude - scene.getLatitude()) < 0.0001 && Math.abs(longtitude - scene.getLongitude()) < 0.0001) {
                    applySetting(scene);
                    break;
                }
            }
        }
    }

    private class WiFiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                Parcelable parcelable = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelable) {
                    NetworkInfo info = (NetworkInfo) parcelable;
                    if (info.getState() == NetworkInfo.State.CONNECTED && info.isAvailable() && info.getType() == ConnectivityManager.TYPE_WIFI) {
                        String name = MobileSettingManager.getWiFiName(mContext);
                        Logger.d("it is this wlan, start "+name);
                        synchronized (wlanMinotor) {
                            List<Scene> wlanScenes = mScenes.get(Constants.WLAN_CODE);
                            for (Scene scene : wlanScenes) {
                                if (scene.getWifiname().equals(name)) {
                                    applySetting(scene);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
