package com.windylee.scene.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.windylee.scene.R;
import com.windylee.scene.SceneFactory;
import com.windylee.scene.entity.NeedReloadEvent;
import com.windylee.scene.entity.AnsEntity;
import com.windylee.scene.utils.PhoneFormatCheckUtils;
import com.windylee.scene.utils.SharePreUtil;
import com.windylee.scene.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windylee on 4/13/17.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_username)
    EditText usernameView;
    @Bind(R.id.et_password)
    EditText passwordView;
    @Bind(R.id.bt_go)
    Button loginView;
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        initToolBar(toolbar, "登录", R.mipmap.icon_arrow_back);
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab,
                            fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
//                Intent i2 = new Intent(this, LoginSuccessActivity.class);
//                startActivity(i2, oc2.toBundle());
                String phone = usernameView.getText().toString();
                String password = passwordView.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showToast(this, "请填写手机号码");
                } else if (TextUtils.isEmpty(password)) {
                    ToastUtils.showToast(this, "请填写密码");
                } else if (!PhoneFormatCheckUtils.isChinaPhoneLegal(phone)) {
                    ToastUtils.showToast(this, "手机号码格式错误");
                } else {
                    SceneFactory.getSceneService().login(phone, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
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
                                    if (ansEntity.isSuccess()) {
                                        SharePreUtil.saveIntData(LoginActivity.this, "userId", ansEntity.getUserId());
                                        EventBus.getDefault().post(new NeedReloadEvent());
                                        finish();
                                    } else {
                                        ToastUtils.showToast(LoginActivity.this, "用户名或密码错误");
                                    }
                                }
                            });
                }
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
                break;
        }
        return false;
    }
}
