package com.windylee.scene.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.windylee.scene.R;
import com.windylee.scene.ui.fragment.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windylee on 4/13/17.
 */

public class SettingActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initToolBar(toolbar, "设置", R.mipmap.icon_arrow_back);
        getFragmentManager().beginTransaction().replace(R.id.setting_content, new SettingFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
