package com.windylee.scene;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.windylee.scene.service.NewsService;
import com.windylee.scene.service.SceneService;
import com.windylee.scene.service.API;
import com.windylee.scene.service.WeatherService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by windylee on 4/13/17.
 */

public class SceneRetrofit {

    private SceneService mSceneService;
    private NewsService mNewsService;
    private WeatherService mWeatherService;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    SceneRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(12, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(API.ZHIHU_BASIC_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit newsRest = builder.build();

        builder.baseUrl(API.SCENE_URL);
        Retrofit sceneRest = builder.build();

        builder.baseUrl(API.URL_Mob);
        Retrofit weatherRest = builder.build();

        mNewsService = newsRest.create(NewsService.class);
        mSceneService = sceneRest.create(SceneService.class);
        mWeatherService = weatherRest.create(WeatherService.class);
    }

    public SceneService getSceneService() {
        return mSceneService;
    }

    public NewsService getNewsService() {
        return mNewsService;
    }

    public WeatherService getWeatherService() {
        return mWeatherService;
    }

}
