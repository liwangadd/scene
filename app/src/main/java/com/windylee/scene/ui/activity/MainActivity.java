package com.windylee.scene.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;
import com.windylee.scene.R;
import com.windylee.scene.SceneFactory;
import com.windylee.scene.entity.Constants;
import com.windylee.scene.entity.WeatherEntity;
import com.windylee.scene.service.API;
import com.windylee.scene.service.ApplySettingService;
import com.windylee.scene.service.AsyncService;
import com.windylee.scene.service.LocateService;
import com.windylee.scene.ui.fragment.CollectFragment;
import com.windylee.scene.ui.fragment.ModeFragment;
import com.windylee.scene.ui.fragment.ReadFragment;
import com.windylee.scene.utils.IntentUtils;
import com.windylee.scene.utils.SharePreUtil;
import com.windylee.scene.utils.ToastUtils;
import com.windylee.scene.widget.CircleImageView;
import com.windylee.scene.widget.fab.FloatingActionButton;
import com.windylee.scene.widget.fab.FloatingActionMenu;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements AMapLocationListener {

    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.menu_red)
    FloatingActionMenu menuRed;
    @Bind(R.id.fab1)
    FloatingActionButton fab1;
    @Bind(R.id.fab2)
    FloatingActionButton fab2;
    @Bind(R.id.fab3)
    FloatingActionButton fab3;
    @Bind(R.id.fab4)
    FloatingActionButton fab4;

    CircleImageView iconView;
    TextView weatherView;
    ImageView weatherIcon;
    TextView temperatureView;
    TextView cityView;
    TextView loginStatusView;

    private ModeFragment modeFragment;
    private ReadFragment readFragment;
    private CollectFragment collectFragment;

    private LocateService mLocateService;
    private int navigationCheckedItemId = R.id.nav_fuli;
    private String navigationCheckedTitle = "模式";

    private boolean isExit = false;
    private String cityName;
    private String provinceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActionBarDrawerToggle actionToogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.app_name, R.string.app_name);
        actionToogle.syncState();
        drawerLayout.setDrawerListener(actionToogle);
        initToolBar(toolbar, "情景模式", -1);

        mLocateService = new LocateService(this);
        mLocateService.requestLocation(this);

        initNavigationView();

        menuRed.setClosedOnTouchOutside(true);

        setDefaultFragment();

        /**
         * 开启后台服务service
         */
        Intent intent = new Intent(this, ApplySettingService.class);
        startService(intent);
        /**
         * 开启同步service
         */
        intent = new Intent(this, AsyncService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SharePreUtil.getIntData(this, "userId", -1) != -1) {
            loginStatusView.setText("已登录");
        }else{
            loginStatusView.setText("未登录");
        }
    }

    private void setDefaultFragment() {
        setMenuSelection(navigationCheckedItemId);
    }

    private void setMenuSelection(int flag) {
        toolbar.setTitle(navigationCheckedTitle);
        // 开启一个Fragment事务
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        menuRed.setVisibility(View.GONE);
        switch (flag) {
            case R.id.nav_fuli:
                menuRed.setVisibility(View.VISIBLE);
                if (modeFragment == null) {
                    modeFragment = new ModeFragment();
                    fragmentTransaction.add(R.id.frame_content, modeFragment);
                } else {
                    fragmentTransaction.show(modeFragment);
                }
                break;
            case R.id.nav_history:
                if (readFragment == null) {
                    readFragment = new ReadFragment();
                    fragmentTransaction.add(R.id.frame_content, readFragment);
                } else {
                    fragmentTransaction.show(readFragment);
                }
                break;
            case R.id.nav_collect:
                if (collectFragment == null) {
                    collectFragment = new CollectFragment();
                    fragmentTransaction.add(R.id.frame_content, collectFragment);
                } else {
                    fragmentTransaction.show(collectFragment);
                }
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (modeFragment != null) {
            transaction.hide(modeFragment);
        }
        if (readFragment != null) {
            transaction.hide(readFragment);
        }
        if (collectFragment != null) {
            transaction.hide(collectFragment);
        }
        /*if (timeFragment != null) {
            transaction.hide(timeFragment);
        }*/
    }

    //初始化抽屉点击响应
    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setTitle(item.getTitle());
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_fuli:
                    case R.id.nav_history:
                    case R.id.nav_collect:
                        navigationCheckedItemId = item.getItemId();
                        navigationCheckedTitle = item.getTitle().toString();
                        setMenuSelection(item.getItemId());
                        break;
                    case R.id.about:
                        item.setChecked(false);
                        IntentUtils.startAboutActivity(MainActivity.this);
                        //跳转到关于界面
                        break;
                    case R.id.setting:
                        item.setChecked(false);
                        IntentUtils.startSettingActivity(MainActivity.this);
                        //跳转到设置界面
                        break;
                    case R.id.share_app:
                        item.setChecked(false);
                        IntentUtils.startAppShareText(MainActivity.this, "干货营", "干货营Android客户端：http://www.baidu.com");
                        //跳转到分享界面
                        break;
                    case R.id.feedback:
                        item.setChecked(false);
                        //跳转到反馈界面
                        IntentUtils.startFeedbackActivity(MainActivity.this);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
        iconView = (CircleImageView) view.findViewById(R.id.user_icon);
        weatherView = (TextView) view.findViewById(R.id.tv_weather);
        weatherIcon = (ImageView) view.findViewById(R.id.iv_weather);
        temperatureView = (TextView) view.findViewById(R.id.tv_temperature);
        cityView = (TextView) view.findViewById(R.id.tv_city);
        loginStatusView = (TextView) view.findViewById(R.id.is_login);

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharePreUtil.getIntData(MainActivity.this,"userId", -1)==-1) {
                    IntentUtils.startLoginActivity(MainActivity.this);
                }else{

                }
            }
        });
    }

    @OnClick({R.id.fab1, R.id.fab2, R.id.fab3, R.id.fab4})
    public void onBtClicked(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.fab1:
                bundle.putInt("type", Constants.NORMAL_CODE);
                break;
            case R.id.fab2:
                bundle.putInt("type", Constants.WLAN_CODE);
                break;

            case R.id.fab3:
                bundle.putInt("type", Constants.LOCATE_CODE);
                break;
            case R.id.fab4:
                bundle.putInt("type", Constants.TIME_CODE);
        }
        menuRed.close(true);
        IntentUtils.startEditActivity(this, bundle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        } else {
            if (!isExit) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                isExit = true;
                ToastUtils.showToast(this, "再按一次退出");
                return;
            }
        }
        super.onBackPressed();
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //停止定位后，本地定位服务并不会被销毁
//                if (mlocationClient != null) {
//                    mlocationClient.stopLocation();
//                }
                cityName = amapLocation.getCity();
                if (cityName.endsWith("市")) {
                    cityName = cityName.substring(0, cityName.length() - 1);
                }
                provinceName = amapLocation.getProvince();
                if (provinceName.endsWith("省") || provinceName.endsWith("市")) {
                    provinceName = provinceName.substring(0, provinceName.length() - 1);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Logger.d("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
        SceneFactory.getWeatherService().getWeatherInfo(API.WEATHER_KEY, cityName, provinceName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherEntity weatherBaseEntity) {
                        WeatherEntity.WeatherBean mBean = weatherBaseEntity.getResult().get(0);
                        temperatureView.setText(mBean.getTemperature());
                        cityView.setText(mBean.getCity());
                        weatherView.setText(mBean.getWeather());
                        weatherIcon.setImageResource(Constants.weatherIcons.get(mBean.getWeather()));
                    }
                });
    }
}
