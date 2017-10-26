package com.windylee.scene.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.windylee.scene.R;
import com.windylee.scene.application.APP;
import com.windylee.scene.entity.ConnEvent;
import com.windylee.scene.entity.Constants;
import com.windylee.scene.entity.NeedReloadEvent;
import com.windylee.scene.entity.Scene;
import com.windylee.scene.ui.adapter.ModeAdapter;
import com.windylee.scene.widget.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by windylee on 4/18/17.
 */

public class ModeContentFragment extends BaseFragment implements ModeAdapter.onModeItemClickListener {

    private static final String TYPE = "type";
    private int type;

    @Bind(R.id.mode_list)
    RecyclerView mDataView;
    @Bind(R.id.mode_empty)
    View emptyView;

    List<Scene> mDatas = new ArrayList<>();
    private ModeAdapter mModeAdapter;

    public static Fragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        ModeContentFragment mFragment = new ModeContentFragment();
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_mode_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        type = bundle.getInt(TYPE);
        SpacesItemDecoration decoration = new SpacesItemDecoration(5);
        mDataView.addItemDecoration(decoration);
        mDataView.setLayoutManager(new LinearLayoutManager(mActivity));
        mModeAdapter = new ModeAdapter(mActivity, mDatas, type);
        mModeAdapter.setOnModeItemClickListener(this);
        mDataView.setAdapter(mModeAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        mDatas = APP.mDb.query(new QueryBuilder<Scene>(Scene.class)
                .where("type = ?", type).appendOrderDescBy("is_open"));
        if (mDatas.size() > 0) {
            emptyView.setVisibility(View.GONE);
            mModeAdapter.setModeItems(mDatas);
        } else emptyView.setVisibility(View.VISIBLE);
    }


    @Override
    public void OnModeItemClicked(int position, int modeId, boolean isOpen) {
        ConnEvent mConnEvent;
        mConnEvent = new ConnEvent(type, mDatas.get(position), isOpen);
//        Logger.d(mDatas.get(position));
        EventBus.getDefault().post(mConnEvent);

        if (type != Constants.NORMAL_CODE) {
            mDatas.get(position).setOpen(isOpen);
            APP.mDb.update(mDatas.get(position));
        }
    }
}
