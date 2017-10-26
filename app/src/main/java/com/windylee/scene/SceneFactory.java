package com.windylee.scene;

import com.windylee.scene.service.NewsService;
import com.windylee.scene.service.SceneService;
import com.windylee.scene.service.WeatherService;

/**
 * Created by windylee on 4/13/17.
 */

public class SceneFactory {

    protected static final Object monitor = new Object();
    static SceneService mSceneService = null;
    static NewsService mNewsService = null;
    static WeatherService mWeatherService;

    public static SceneService getSceneService() {
        synchronized (monitor) {
            if (mSceneService == null) {
                mSceneService = new SceneRetrofit().getSceneService();
            }
            return mSceneService;
        }
    }

    public static NewsService getNewsService() {
        synchronized (monitor) {
            if (mNewsService == null) {
                mNewsService = new SceneRetrofit().getNewsService();
            }
            return mNewsService;
        }
    }

    public static WeatherService getWeatherService() {
        synchronized (monitor) {
            if (mWeatherService == null) {
                mWeatherService = new SceneRetrofit().getWeatherService();
            }
            return mWeatherService;
        }
    }

}
