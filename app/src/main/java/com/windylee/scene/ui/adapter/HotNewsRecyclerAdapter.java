package com.windylee.scene.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.windylee.scene.R;
import com.windylee.scene.entity.HotNews;
import com.windylee.scene.ui.activity.BaseActivity;

import java.util.List;

/**
 * Created by windylee on 4/12/17.
 */

public class HotNewsRecyclerAdapter extends RecyclerView.Adapter<HotNewsRecyclerAdapter.ViewHolder> {

    private List<HotNews.RecentBean> mBean;
    private LayoutInflater mInflate;
    private OnHotItemClickListener mListener;
    private Context mContext;

    public HotNewsRecyclerAdapter(BaseActivity mActivity, List<HotNews.RecentBean> mRecent) {
        this.mBean = mRecent;
        mContext = mActivity;
        mInflate = LayoutInflater.from(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflate.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Picasso.with(mContext).load(mBean.get(position).getThumbnail())
                .into(holder.mImageView);
//        holder.mImageView.setImageResource(R.mipmap.ic_launcher);
        holder.mTextView.setText(mBean.get(position).getTitle());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onHotItemClick(v, mBean.get(position));
                }
            }
        });
    }

    public void changeListData(List<HotNews.RecentBean> bean) {
        mBean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        return mBean.size();
        return mBean.size();
    }

    public void setHotItemNewsClickListener(OnHotItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnHotItemClickListener {
        void onHotItemClick(View view, HotNews.RecentBean bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        ImageView mImageView;
        View item;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_item);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item);
            item = itemView;
        }

    }
}
