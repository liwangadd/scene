package com.windylee.scene.service;

import com.windylee.scene.entity.Scene;
import com.windylee.scene.entity.AnsEntity;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by windylee on 4/13/17.
 */

public interface SceneService {

    @POST("/user/register")
    Observable<AnsEntity> register(@Query("username") String username, @Query("password") String password);

    @POST("/user/login")
    Observable<AnsEntity> login(@Query("username") String username, @Query("password") String password);

    @POST("/mode/save")
    Observable<AnsEntity> saveMode(@Body Scene scene);

    @POST("/mode/getModes")
    Observable<List<Scene>> getAllMOdes(@Query("user_id") int userId);

}
