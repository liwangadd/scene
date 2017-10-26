package com.windylee.scene.application;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.windylee.scene.BuildConfig;

/**
 * Created by windylee on 4/13/17.
 */

public class APP extends Application {

    private static final String DB_NAME = "scene.db";
    public static Context mContext;
    public static LiteOrm mDb;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Logger.init("Scene").methodCount(3).hideThreadInfo()
                .logLevel(LogLevel.FULL).methodOffset(2);
        mDb = LiteOrm.newSingleInstance(this, DB_NAME);
        if (BuildConfig.DEBUG) {
            mDb.setDebugged(true);
        }
    }
}
