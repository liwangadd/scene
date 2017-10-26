package com.windylee.scene.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.windylee.scene.R;
import com.windylee.scene.ui.adapter.ModeAdapter;
import com.windylee.scene.widget.fab.FloatingActionButton;
import com.windylee.scene.widget.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windylee on 4/7/17.
 */

public class ModeFragment extends BaseFragment {

    @Bind(R.id.mode_tab)
    TabLayout mTabLayout;
    @Bind(R.id.mode_content)
    ViewPager mViewPager;

    List<Fragment> mFragments = new ArrayList<>();
    List<String> mTitles = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_mode, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initFragment();

        mViewPager.setAdapter(new ModeTypeAdapter(mActivity.getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    private void initFragment() {
        mTitles.add("普通");
        mTitles.add("WLAN");
        mTitles.add("位置");
        mTitles.add("时间");
        mFragments.add(ModeContentFragment.newInstance(1));
        mFragments.add(ModeContentFragment.newInstance(2));
        mFragments.add(ModeContentFragment.newInstance(3));
        mFragments.add(ModeContentFragment.newInstance(4));
    }

    private class ModeTypeAdapter extends FragmentPagerAdapter{

        ModeTypeAdapter(FragmentManager mManager){
            super(mManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

}
