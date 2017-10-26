package com.windylee.scene.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;

import java.util.List;

/**
 * Created by windylee on 4/12/17.
 */

public class ReadAdapter extends FragmentPagerAdapter {

    private final List<String> titls;
    private List<Fragment> mFragments;

    public ReadAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.mFragments = fragments;
        this.titls = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titls.get(position);
    }

    @Override
    public int getCount() {
        return titls.size();
    }
}
