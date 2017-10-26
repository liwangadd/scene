package com.windylee.scene.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.windylee.scene.SceneFactory;
import com.windylee.scene.entity.HotNews;
import com.windylee.scene.service.API;
import com.windylee.scene.ui.activity.NewsDetailsActivity;
import com.windylee.scene.ui.adapter.HotNewsRecyclerAdapter;
import com.windylee.scene.utils.NetUtils;
import com.windylee.scene.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windylee on 4/12/17.
 */

public class HotNewsFragment extends ReadContentFragment implements SwipeRefreshLayout.OnRefreshListener,
        HotNewsRecyclerAdapter.OnHotItemClickListener {

    public final static String HOT_NEWS_JSON = "hot_news_json";
    public HotNewsRecyclerAdapter mAdapter;

    public HotNewsFragment() {
    }

    @Override
    protected String getFirstPageUrl() {
        return "";
    }

    @Override
    protected void initView(LayoutInflater inflater, ViewGroup container) {
        List<HotNews.RecentBean> mRecent = new ArrayList<>();
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new HotNewsRecyclerAdapter(mActivity, mRecent);
        mAdapter.setHotItemNewsClickListener(this);
        mContentRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void loadDataFromNet(String url) {
        SceneFactory.getNewsService().getHotNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HotNews>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(HotNews hotNews) {
                        if (hotNews != null) {
                            String json = mGson.toJson(hotNews);
                            if (mCache.isNewResponse(HOT_NEWS_JSON, json)) {
                                mCache.put(HOT_NEWS_JSON, json);
                                mAdapter.changeListData(hotNews.getRecent());
                            }
                        }
                        mContentSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        if (!mCache.isCacheEmpty(HOT_NEWS_JSON)) {
            mAdapter.changeListData(new Gson().fromJson(mCache.getAsString(HOT_NEWS_JSON), HotNews.class).getRecent());
        } else {
            if (NetUtils.hasNetWorkConection(mActivity)) {
                mContentSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mContentSwipeRefreshLayout.setRefreshing(true);
                    }
                });
                loadDataFromNet("");
            } else {
                ToastUtils.showToast(mActivity, "网络没有连接哦");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHotItemClick(View view, HotNews.RecentBean bean) {
        int[] clickLocation = getClickLocation(view);
        startActivity(NewsDetailsActivity.newTopStoriesIntent(mActivity, API.ZHIHU_NEWS_TWO, (String.valueOf(bean.getNewsId())),
                clickLocation));
        mActivity.overridePendingTransition(0, 0);
        //ToastUtils.showToast(mActivity, "item clicked");
    }
}
