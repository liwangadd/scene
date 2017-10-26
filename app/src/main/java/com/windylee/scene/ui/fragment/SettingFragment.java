package com.windylee.scene.ui.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.windylee.scene.R;
import com.windylee.scene.utils.IntentUtils;
import com.windylee.scene.utils.ToastUtils;

/**
 * Created by windylee on 4/13/17.
 */

public class SettingFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initListener();
    }

    private void initListener() {

        findPreference("rm_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtils.showToast(getActivity(), "缓存已清空");
                return true;
            }
        });

        findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                IntentUtils.startFeedbackActivity(getActivity());
                return true;
            }
        });

        findPreference("check_update").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtils.showToast(getActivity(), "已是最新版本");
                return true;
            }
        });

        findPreference("current_version").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return true;
            }
        });
    }

}
