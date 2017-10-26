package com.windylee.scene.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.windylee.scene.R;

/**
 * Created by windylee on 4/11/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initToolBar(Toolbar toolbar, String title, int icon) {
        toolbar.setTitle(title);// 标题的文字需在setSupportActionBar之前，不然会无效
        if (icon != -1) {
            toolbar.setNavigationIcon(R.mipmap.icon_arrow_back);
        }
        setSupportActionBar(toolbar);
    }

}

