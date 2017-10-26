package com.windylee.scene.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.windylee.scene.SceneFactory;
import com.windylee.scene.application.APP;
import com.windylee.scene.entity.NeedReloadEvent;
import com.windylee.scene.entity.Scene;
import com.windylee.scene.entity.AnsEntity;
import com.windylee.scene.utils.SharePreUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windy on 17/4/22.
 */

public class AsyncService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void uploadScene(Scene scene) {
        if (SharePreUtil.getIntData(this, "userId", -1) != -1) {
            scene.setUserId(SharePreUtil.getIntData(this, "userId", -1));
            SceneFactory.getSceneService().saveMode(scene)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AnsEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.d(e.getMessage());
                        }

                        @Override
                        public void onNext(AnsEntity ansEntity) {
                            Logger.d(ansEntity.isSuccess());
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getAllModes(NeedReloadEvent event) {
        SceneFactory.getSceneService().getAllMOdes(SharePreUtil.getIntData(this, "userId", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Scene>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Scene> scenes) {
                        APP.mDb.insert(scenes);
//                        EventBus.getDefault().post(new NeedReloadEvent());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
