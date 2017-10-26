package com.windylee.scene.entity;

import com.windylee.scene.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by windylee on 4/15/17.
 */

public class Constants {

    public static Map<String, Integer> weatherIcons = new HashMap<>();
    public static List<String> ringModes = Arrays.asList("保持原有", "静音", "震动", "铃声");
    public static List<String> lightModes = Arrays.asList("保持原有", "手动", "自动");
    public static List<String> normalMode = Arrays.asList("保持原有", "开", "关");

    public static final int NORMAL_CODE = 1;
    public static final int WLAN_CODE = 2;
    public static final int LOCATE_CODE = 3;
    public static final int TIME_CODE = 4;
    public static final int RELOAD_CODE=5;

    static {
        weatherIcons.put("未知", R.mipmap.icon_weather_none);
        weatherIcons.put("晴", R.mipmap.icon_weather_sunny);
        weatherIcons.put("阴", R.mipmap.icon_weather_cloudy1);
        weatherIcons.put("多云", R.mipmap.icon_weather_cloudy2);
        weatherIcons.put("少云", R.mipmap.icon_weather_cloudy3);
        weatherIcons.put("晴间多云", R.mipmap.icon_weather_cloudytosunny);
        weatherIcons.put("局部多云", R.mipmap.icon_weather_cloudytosunny);
        weatherIcons.put("雨", R.mipmap.icon_weather_rain1);
        weatherIcons.put("小雨", R.mipmap.icon_weather_rain1);
        weatherIcons.put("中雨", R.mipmap.icon_weather_rain2);
        weatherIcons.put("大雨", R.mipmap.icon_weather_rain4);
        weatherIcons.put("阵雨", R.mipmap.icon_weather_rain5);
        weatherIcons.put("雷阵雨", R.mipmap.icon_weather_rain5);
        weatherIcons.put("霾", R.mipmap.icon_weather_haze);
        weatherIcons.put("雾", R.mipmap.icon_weather_fog);
        weatherIcons.put("雨夹雪", R.mipmap.icon_weather_snowrain);
    }

}
