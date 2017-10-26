package com.windylee.scene.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.orhanobut.logger.Logger;
import com.windylee.scene.R;
import com.windylee.scene.application.APP;
import com.windylee.scene.entity.NewsDetails;
import com.windylee.scene.ui.activity.CollectDetailsActivity;
import com.windylee.scene.ui.adapter.CollectRecyclerAdapter;
import com.windylee.scene.widget.PullLoadRecyclerView;
import com.windylee.scene.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windylee on 4/12/17.
 */

public class CollectFragment extends BaseFragment implements CollectRecyclerAdapter.OnCollectItemClickListener {

    @Bind(R.id.collect_empty)
    View emptyView;
    @Bind(R.id.collect_content)
    PullLoadRecyclerView mContentRecyclerView;

    private List<NewsDetails> datas = new ArrayList<>();
    private CollectRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        mContentRecyclerView.addItemDecoration(decoration);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new CollectRecyclerAdapter(mActivity, datas);
        mAdapter.setCollectItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        datas = APP.mDb.query(new QueryBuilder<NewsDetails>(NewsDetails.class));
        if (datas.size() != 0) {
            mAdapter.changeListData(datas);
            mContentRecyclerView.setAdapter(mAdapter);
        } else {
            mContentRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCollectItemClick(View view, NewsDetails bean) {
        startActivity(CollectDetailsActivity.newTopStoriesIntent(mActivity, bean, getClickLocation(view)));
        mActivity.overridePendingTransition(0, 0);
    }

    protected int[] getClickLocation(View view) {
        int[] clickLocation = new int[2];
        view.getLocationOnScreen(clickLocation);
        clickLocation[0] += view.getWidth() / 2;
        return clickLocation;
    }
}
