package com.windylee.scene.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.windylee.scene.R;
import com.windylee.scene.utils.ASimpleCache;
import com.windylee.scene.utils.CacheUtil;
import com.windylee.scene.utils.NetUtils;
import com.windylee.scene.utils.ToastUtils;
import com.windylee.scene.widget.PullLoadRecyclerView;
import com.windylee.scene.widget.SpacesItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windylee on 4/12/17.
 */

public abstract class ReadContentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    //数据是否加载完毕
    protected boolean isDataLoaded = false;
    //视图是否创建完毕
    protected boolean isViewCreated = false;
    protected CacheUtil mCache;
    protected Gson mGson;
    @Bind(R.id.srl_content)
    protected SwipeRefreshLayout mContentSwipeRefreshLayout;
    @Bind(R.id.rv_content)
    protected PullLoadRecyclerView mContentRecyclerView;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x122:
                    mContentSwipeRefreshLayout.setRefreshing(false);
                    ToastUtils.showToast(mActivity, "网络没有连接哦");
                    break;
                case 0x121:
                    loadDataFromNet(getFirstPageUrl());
            }
        }
    };

    public ReadContentFragment() {
    }

    protected abstract String getFirstPageUrl();

    protected boolean isFirstPage(String url) {
        return getFirstPageUrl().equals(url);
    }

    protected abstract void loadDataFromNet(String url);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        mContentRecyclerView.addItemDecoration(decoration);

        initView(inflater, container);
        initSwipeRefreshLayout();

        isViewCreated = true;

        return view;
    }

    protected abstract void initView(LayoutInflater inflater, ViewGroup container);

    protected void initData() {
        isDataLoaded=true;
        mCache = CacheUtil.getInstance(mActivity);
        mGson = new Gson();
    }

    public void initSwipeRefreshLayout() {
        mContentSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light, android.R.color.holo_green_light, android.R.color.holo_red_dark);
        mContentSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isDataLoaded && isViewCreated) {
            initData();
        }
    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasNetWorkConection(mActivity)) {
            mHandler.sendEmptyMessage(0x121);
        } else {
            mHandler.sendEmptyMessage(0x122);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            initData();
        }
    }

    protected int[] getClickLocation(View view) {
        int[] clickLocation = new int[2];
        view.getLocationOnScreen(clickLocation);
        clickLocation[0] += view.getWidth() / 2;
        return clickLocation;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mContentSwipeRefreshLayout.isRefreshing()) {
            mContentSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
}