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
import com.windylee.scene.entity.DailyNews;

import java.util.List;

/**
 * Created by windylee on 4/12/17.
 */

public class DailyNewsRecyclerAdapter extends RecyclerView.Adapter<DailyNewsRecyclerAdapter.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;
    private List<DailyNews.StoriesBean> mBean;
    private LayoutInflater mInflater;
    private OnDailyItemClickListener mListener;
    private View mHeaderView;
    private Context mContext;

    public DailyNewsRecyclerAdapter(Context context, List<DailyNews.StoriesBean> storiesBeen) {
        mBean = storiesBeen;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        return new ViewHolder(mInflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        final int newPosition = getNewPosition(holder);
        Picasso.with(mContext).load(mBean.get(newPosition).getImages().get(0))
                .into(holder.mImageView);
//        ImageUtil.getInstance().displayImageOnLoading(mBean.get(newPosition).getImages().get(0), holder
//                .mImageView);
        holder.mTextView.setText(mBean.get(newPosition).getTitle());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDailyItemClick(v, mBean.get(newPosition));
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mHeaderView != null && position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    //    添加头布局后，count 应该 +1
    @Override
    public int getItemCount() {
        return mHeaderView == null ? mBean.size() : mBean.size() + 1;
    }

    //    添加头布局后的新 position
    public int getNewPosition(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        return mHeaderView == null ? position : position - 1;
    }

    public void addListData(List<DailyNews.StoriesBean> bean) {
        mBean.addAll(bean);
        notifyDataSetChanged();
    }

    public void clearList() {
        mBean.clear();
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    public void setOnDailyItemClickListener(OnDailyItemClickListener listener) {
        mListener = listener;
    }

    public interface OnDailyItemClickListener {
        void onDailyItemClick(View view, DailyNews.StoriesBean bean);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        View item;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            mTextView = (TextView) itemView.findViewById(R.id.tv_item);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item);
            item = itemView;
        }
    }
}
