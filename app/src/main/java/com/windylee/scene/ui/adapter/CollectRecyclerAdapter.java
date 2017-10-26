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
import com.windylee.scene.entity.NewsDetails;
import com.windylee.scene.ui.activity.BaseActivity;

import java.util.List;

/**
 * Created by windylee on 4/24/17.
 */

public class CollectRecyclerAdapter extends RecyclerView.Adapter<CollectRecyclerAdapter.ViewHolder> {

    private List<NewsDetails> mBean;
    private LayoutInflater mInflate;
    private CollectRecyclerAdapter.OnCollectItemClickListener mListener;
    private Context mContext;

    public CollectRecyclerAdapter(BaseActivity mActivity, List<NewsDetails> mRecent) {
        this.mBean = mRecent;
        mContext = mActivity;
        mInflate = LayoutInflater.from(mActivity);
    }

    @Override
    public CollectRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectRecyclerAdapter.ViewHolder(mInflate.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(CollectRecyclerAdapter.ViewHolder holder, final int position) {
        Picasso.with(mContext).load(mBean.get(position).getImage())
                .into(holder.mImageView);
//        holder.mImageView.setImageResource(R.mipmap.ic_launcher);
        holder.mTextView.setText(mBean.get(position).getTitle());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCollectItemClick(v, mBean.get(position));
                }
            }
        });
    }

    public void changeListData(List<NewsDetails> bean) {
        mBean = bean;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        return mBean.size();
        return mBean.size();
    }

    public void setCollectItemClickListener(CollectRecyclerAdapter.OnCollectItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnCollectItemClickListener {
        void onCollectItemClick(View view, NewsDetails bean);
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
