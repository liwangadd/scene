package com.windylee.scene.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.windylee.scene.R;
import com.windylee.scene.application.APP;
import com.windylee.scene.entity.Constants;
import com.windylee.scene.entity.NeedReloadEvent;
import com.windylee.scene.entity.Scene;
import com.windylee.scene.service.LocateService;
import com.windylee.scene.utils.DialogUtils;
import com.windylee.scene.utils.MobileSettingManager;
import com.windylee.scene.utils.ToastUtils;
import com.windylee.scene.utils.VolumeManager;
import com.windylee.scene.widget.seekbar.BubbleSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by windylee on 4/18/17.
 */

public class EditModeActivity extends BaseActivity implements DialogInterface.OnClickListener, MaterialDialog.ListCallback,
        AMapLocationListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.mode_name_bg)
    View modeNameBg;
    @Bind(R.id.mode_name_tv)
    TextView modeNameTv;
    @Bind(R.id.mode_desc_bg)
    View modeDescBg;
    @Bind(R.id.mode_desc_tv)
    TextView modeDescTv;
    @Bind(R.id.alarm_volume_bg)
    View alarmBg;
    @Bind(R.id.alarm_volume_tv)
    TextView alarmTv;
    @Bind(R.id.media_volume_bg)
    View mediaBg;
    @Bind(R.id.media_volume_tv)
    TextView mediaTv;
    @Bind(R.id.ring_volume_bg)
    View ringBg;
    @Bind(R.id.ring_volume_tv)
    TextView ringTv;
    @Bind(R.id.ring_mode_bg)
    View ringModeBg;
    @Bind(R.id.ring_mode_tv)
    TextView ringModeTv;
    @Bind(R.id.ring_change_bg)
    View ringChangeBg;
    @Bind(R.id.light_change_bg)
    View lightChangebg;
    @Bind(R.id.light_change_tv)
    TextView lightChangeTv;
    @Bind(R.id.light_bg)
    View lightBg;
    @Bind(R.id.light_tv)
    TextView lightTv;
    @Bind(R.id.auto_rotate_bg)
    View screenRotateBg;
    @Bind(R.id.auto_rotate_tv)
    TextView screenRotateTv;
    @Bind(R.id.wifi_bg)
    View wifiBg;
    @Bind(R.id.wifi_tv)
    TextView wifiTv;
    @Bind(R.id.gprs_bg)
    View gprsBg;
    @Bind(R.id.gprs_tv)
    TextView gprsTv;
    @Bind(R.id.bluetooth_bg)
    View bluetoothBg;
    @Bind(R.id.bluetooth_tv)
    TextView bluetoothTv;
    @Bind(R.id.mode_desc_name)
    TextView modeDescNameTv;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    BubbleSeekBar mBubbleSeekBar;
    int whichSetting;
    private Scene mScene = new Scene();
    private VolumeManager mVolumeManager;
    private LocateService mLocateService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);
        ButterKnife.bind(this);

        mScene.setType(getIntent().getIntExtra("type", -1));
        setDefaultScene();

        initToolBar(toolbar, "模式编辑", R.mipmap.icon_arrow_back);
    }

    @Override
    public void initToolBar(Toolbar toolbar, String title, int icon) {
        super.initToolBar(toolbar, title, icon);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (TextUtils.isEmpty(mScene.getSceneName())) {
                    ToastUtils.showToast(EditModeActivity.this, "请填写模式名称");
                } else if (TextUtils.isEmpty(mScene.getSceneDesc())) {
                    if (mScene.getType() == Constants.LOCATE_CODE)
                        ToastUtils.showToast(EditModeActivity.this, "请先定位");
                    else if (mScene.getType() == Constants.TIME_CODE)
                        ToastUtils.showToast(EditModeActivity.this, "请确定时间");
                    else
                        ToastUtils.showToast(EditModeActivity.this, "请填写模式描述");
                } else {
                    APP.mDb.insert(mScene);
                    ToastUtils.showToast(EditModeActivity.this, "保存成功");
//                    EventBus.getDefault().post(new NeedReloadEvent());
                    EventBus.getDefault().post(mScene);
                    finish();
                }
                return true;
            }
        });
    }

    private void setDefaultScene() {
        mVolumeManager = new VolumeManager(this);
        mScene.setAlarmVolume(mVolumeManager.getAlarmVolume());
        mScene.setMediaVolume(mVolumeManager.getMusicVolume());
        mScene.setRingmode(mVolumeManager.getRingVolume());
        mScene.setRingmode(0);
        mScene.setLightmode(0);
        mScene.setLightness(MobileSettingManager.getScreenLight(this));
        mScene.setAutoRotate(0);
        mScene.setWifi(0);
        mScene.setGprs(0);
//        mScene.setWifiHot(0);
        mScene.setBluetooth(0);

        mLocateService = new LocateService(this);

        if (mScene.getType() == Constants.LOCATE_CODE) {
//            mLocateService.requestLocation(this, this);
            modeDescNameTv.setText("定位");
        } else if (mScene.getType() == Constants.TIME_CODE) {
            modeDescNameTv.setText("确定时间");
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
            String currentTime = sf.format(date);
            modeDescTv.setText(currentTime);
        } else if (mScene.getType() == Constants.WLAN_CODE) {
            modeDescTv.setText(MobileSettingManager.getWiFiName(this));
            mScene.setSceneDesc(modeDescTv.getText().toString());
            mScene.setWifiname(modeDescTv.getText().toString());
        }

        alarmTv.setText(String.valueOf(mScene.getAlarmVolume()));
        mediaTv.setText(String.valueOf(mScene.getMediaVolume()));
        ringTv.setText(String.valueOf(mScene.getRingVolume()));
        lightTv.setText(String.valueOf(mScene.getLightness()));
    }

    @OnClick({R.id.mode_name_bg, R.id.mode_desc_bg, R.id.alarm_volume_bg, R.id.media_volume_bg, R.id.ring_volume_bg, R.id.ring_mode_bg, R.id.ring_change_bg,
            R.id.light_change_bg, R.id.light_bg, R.id.auto_rotate_bg, R.id.wifi_bg, R.id.gprs_bg, R.id.bluetooth_bg})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.mode_name_bg:
                DialogUtils.showInputDialog(this, "请输入模式名称", modeNameTv.getText().toString(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        modeNameTv.setText(input);
                        mScene.setSceneName(input.toString());
                    }
                });
                break;
            case R.id.mode_desc_bg:
                onClickDescItem();
                break;
            case R.id.alarm_volume_bg:
                whichSetting = 1;
                mBubbleSeekBar = DialogUtils.showSeekBarDialog(this, "闹钟音量", true, mScene.getAlarmVolume(), mVolumeManager.getMaxAlarmVolume(), this);
                break;
            case R.id.media_volume_bg:
                whichSetting = 2;
                mBubbleSeekBar = DialogUtils.showSeekBarDialog(this, "媒体音量", true, mScene.getMediaVolume(), mVolumeManager.getMaxMusicVolume(), this);
                break;
            case R.id.ring_volume_bg:
                whichSetting = 3;
                mBubbleSeekBar = DialogUtils.showSeekBarDialog(this, "来电音量", true, mScene.getRingVolume(), mVolumeManager.getMaxRingVolume(), this);
                break;
            case R.id.ring_mode_bg:
                whichSetting = 4;
                DialogUtils.showListDialog(this, "铃声模式", Constants.ringModes, this);
                break;
            case R.id.ring_change_bg:

                break;
            case R.id.light_change_bg:
                whichSetting = 5;
                DialogUtils.showListDialog(this, "亮度调节", Constants.lightModes, this);
                break;
            case R.id.light_bg:
                whichSetting = 6;
                mBubbleSeekBar = DialogUtils.showSeekBarDialog(this, "屏幕亮度", false, mScene.getLightness(), 255, this);
                break;
            case R.id.auto_rotate_bg:
                whichSetting = 7;
                DialogUtils.showListDialog(this, "自动转屏", Constants.normalMode, this);
                break;
            case R.id.wifi_bg:
                whichSetting = 8;
                DialogUtils.showListDialog(this, "WiFi", Constants.normalMode, this);
                break;
            case R.id.gprs_bg:
                whichSetting = 9;
                DialogUtils.showListDialog(this, "数据网络(需要root)", Constants.normalMode, this);
                break;
            case R.id.bluetooth_bg:
                whichSetting = 11;
                DialogUtils.showListDialog(this, "蓝牙", Constants.normalMode, this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mode_save_menu, menu);
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (whichSetting) {
            case 1:
                mScene.setAlarmVolume(mBubbleSeekBar.getProgress());
                alarmTv.setText(String.valueOf(mBubbleSeekBar.getProgress()));
                break;
            case 2:
                mScene.setMediaVolume(mBubbleSeekBar.getProgress());
                mediaTv.setText(String.valueOf(mBubbleSeekBar.getProgress()));
                break;
            case 3:
                mScene.setRingVolume(mBubbleSeekBar.getProgress());
                ringTv.setText(String.valueOf(mBubbleSeekBar.getProgress()));
                break;
            case 6:
                mScene.setLightness(mBubbleSeekBar.getProgress());
                lightTv.setText(String.valueOf(mBubbleSeekBar.getProgress()));
                break;
        }
    }

    @Override
    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
        switch (whichSetting) {
            case 4:
                ringModeTv.setText(Constants.ringModes.get(position));
                mScene.setRingmode(position);
                break;
            case 5:
                mScene.setLightmode(position);
                lightChangeTv.setText(Constants.lightModes.get(position));
                break;
            case 7:
                screenRotateTv.setText(Constants.normalMode.get(position));
                mScene.setAutoRotate(position);
                break;
            case 8:
                wifiTv.setText(Constants.normalMode.get(position));
                mScene.setWifi(position);
                break;
            case 9:
                gprsTv.setText(Constants.normalMode.get(position));
                mScene.setGprs(position);
                break;
//            case 10:
//                wifiHotTv.setText(Constants.normalMode.get(position));
//                mScene.setWifiHot(position);
//                break;
            case 11:
                bluetoothTv.setText(Constants.normalMode.get(position));
                mScene.setBluetooth(position);
                break;
        }
    }

    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mScene.setLatitude(aMapLocation.getLatitude());
        mScene.setLongitude(aMapLocation.getLongitude());
        mScene.setSceneDesc(aMapLocation.getAddress());
        modeDescTv.setText(mScene.getLatitude() + "-" + mScene.getLongitude());
        mScene.setSceneDesc(modeDescTv.getText().toString());
    }

    private void onClickDescItem() {
        switch (mScene.getType()) {
            case Constants.WLAN_CODE:
            case Constants.NORMAL_CODE:
                DialogUtils.showInputDialog(this, "模式描述", mScene.getSceneDesc(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        mScene.setSceneDesc(input.toString());
                        modeDescTv.setText(input);
                    }
                });
                break;
            case Constants.LOCATE_CODE:
                mLocateService.requestLocation(this);
                ToastUtils.showToast(this, "正在定位");
                break;
            case Constants.TIME_CODE:
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE), true);
                tpd.show(getFragmentManager(), "haha");
                break;
        }
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        mScene.setHour(hourOfDay);
        mScene.setMinute(minute);
        modeDescTv.setText(hourOfDay + ":" + minute);
        mScene.setSceneDesc(modeDescTv.getText().toString());
    }
}
