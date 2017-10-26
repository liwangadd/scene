package com.windylee.scene.service;

import com.windylee.scene.entity.WeatherEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by windylee on 4/13/17.
 */

public interface WeatherService {

    @GET("weather/query")
    Observable<WeatherEntity> getWeatherInfo(@Query("key") String appkey,
                                             @Query("city") String city,
                                             @Query("province") String province);

}
