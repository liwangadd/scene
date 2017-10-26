package com.windylee.scene.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;
import com.windylee.scene.R;
import com.windylee.scene.SceneFactory;
import com.windylee.scene.entity.DailyNews;
import com.windylee.scene.service.API;
import com.windylee.scene.ui.activity.NewsDetailsActivity;
import com.windylee.scene.ui.adapter.DailyNewsRecyclerAdapter;
import com.windylee.scene.ui.adapter.TopNewsHolderView;
import com.windylee.scene.utils.NetUtils;
import com.windylee.scene.utils.ToastUtils;
import com.windylee.scene.widget.PullLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windylee on 4/12/17.
 */

public class DailyNewsFragment extends ReadContentFragment implements OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, DailyNewsRecyclerAdapter.OnDailyItemClickListener {

    public final static String DAYLY_NEWS_JSON = "daily_news_json";
    public String mDate;
    private ConvenientBanner mShowConvenientBanner;
    private List<DailyNews.TopStoriesBean> mTopStories;
    private List<DailyNews.StoriesBean> mNewsStories;
    private DailyNewsRecyclerAdapter mAdapter;

    public DailyNewsFragment() {
    }

    @Override
    protected String getFirstPageUrl() {
        return API.ZHIHU_LATEST;
    }

    @Override
    protected void initView(LayoutInflater inflater, ViewGroup container) {
        mNewsStories = new ArrayList<>();
        mTopStories = new ArrayList<>();
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        View header = inflater.inflate(R.layout.daily_news_header_view, mContentRecyclerView, false);
        mShowConvenientBanner = (ConvenientBanner) header.findViewById(R.id.cb_show);
        //设置适配器
        mAdapter = new DailyNewsRecyclerAdapter(mActivity, mNewsStories);
        mAdapter.setHeaderView(mShowConvenientBanner);
        mAdapter.setOnDailyItemClickListener(this);
        mContentRecyclerView.setAdapter(mAdapter);

        mContentRecyclerView.setPullLoadListener(new PullLoadRecyclerView.onPullLoadListener() {
            @Override
            public void onPullLoad() {
                loadMoreData(mDate);
            }
        });
    }

    protected void loadMoreData(String url) {
        SceneFactory.getNewsService().getMoreDailyNews(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DailyNews>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                    }

                    @Override
                    public void onNext(DailyNews dailyNews) {
                        if (dailyNews != null) {
                            mDate = dailyNews.getDate();
                            mNewsStories = dailyNews.getStories();
                            mAdapter.addListData(mNewsStories);
                        }
                        mContentSwipeRefreshLayout.setRefreshing(false);
                        mContentRecyclerView.setIsLoading(false);
                    }
                });
    }

    @Override
    protected void loadDataFromNet(final String url) {
        SceneFactory.getNewsService().getDailyNews(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DailyNews>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                    }

                    @Override
                    public void onNext(DailyNews dailyNews) {
                        if (dailyNews != null) {
                            String json = mGson.toJson(dailyNews);
                            if (mCache.isNewResponse(DAYLY_NEWS_JSON, json)) {
                                mTopStories = dailyNews.getTopStories();
                                initBanner();
                                mCache.put(DAYLY_NEWS_JSON, json);
                            }
                            mAdapter.clearList();
                            mDate = dailyNews.getDate();
                            mNewsStories = dailyNews.getStories();
                            mAdapter.addListData(mNewsStories);
                        }
                        mContentSwipeRefreshLayout.setRefreshing(false);
                        mContentRecyclerView.setIsLoading(false);
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        if (!mCache.isCacheEmpty(DAYLY_NEWS_JSON)) {
            DailyNews dailyNews = mGson.fromJson(mCache.getAsString(DAYLY_NEWS_JSON), DailyNews.class);
            mTopStories = dailyNews.getTopStories();
            mDate = dailyNews.getDate();
            mAdapter.addListData(dailyNews.getStories());
            initBanner();
        } else {
            if (NetUtils.hasNetWorkConection(mActivity)) {
                mContentSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mContentSwipeRefreshLayout.setRefreshing(true);
                    }
                });
                loadDataFromNet(API.ZHIHU_LATEST);
            } else {
                ToastUtils.showToast(mActivity, "网络没有连接哦");
            }
        }
    }

    public void initBanner() {
        mShowConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new TopNewsHolderView();
            }
        }, mTopStories)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.drawable.indicator_gray, R.drawable.indicator_red})
                .setOnItemClickListener(this);
        mShowConvenientBanner.setScrollDuration(1500);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isDataLoaded && isViewCreated) {
            mShowConvenientBanner.startTurning(4500);
        }
        if (!isVisibleToUser && isViewCreated) {
            mShowConvenientBanner.stopTurning();
        }
    }

    @Override
    public void onItemClick(int position) {
        startActivity(NewsDetailsActivity.newTopStoriesIntent(mActivity, API.ZHIHU_NEWS_FOUR,
                String.valueOf(mTopStories.get(position).getId()), null));
    }

    @Override
    public void onDailyItemClick(View view, DailyNews.StoriesBean bean) {
        int[] clickLocation = getClickLocation(view);
        startActivity(NewsDetailsActivity.newTopStoriesIntent(mActivity, API.ZHIHU_NEWS_FOUR, String.valueOf(bean.getId()), clickLocation));
        mActivity.overridePendingTransition(0, 0);
//        ToastUtils.showToast(mActivity, "item clicked");
    }

    @Override
    public void onResume() {
        super.onResume();
        mShowConvenientBanner.startTurning(4500);
    }

    @Override
    public void onPause() {
        super.onPause();
        mShowConvenientBanner.stopTurning();
    }


}
