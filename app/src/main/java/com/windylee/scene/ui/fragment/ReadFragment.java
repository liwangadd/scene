package com.windylee.scene.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.windylee.scene.R;
import com.windylee.scene.ui.adapter.ReadAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windylee on 4/12/17.
 */

public class ReadFragment extends Fragment {

    @Bind(R.id.read_tab)
    TabLayout readTab;
    @Bind(R.id.read_content)
    ViewPager readContent;

    List<Fragment> mFragments = new ArrayList<>();
    List<String> mTitles = new ArrayList<>();
    ReadAdapter mReadAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initFragment();
    }

    private void initFragment() {
        mFragments.add(new DailyNewsFragment());
        mFragments.add(new HotNewsFragment());
        mTitles.add("知乎日报");
        mTitles.add("热门消息");
        mReadAdapter = new ReadAdapter(getActivity().getSupportFragmentManager(), mFragments, mTitles);
        readContent.setAdapter(mReadAdapter);
        readTab.setSelectedTabIndicatorColor(Color.WHITE);
        readTab.setupWithViewPager(readContent);
    }
}