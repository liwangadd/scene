package com.windylee.scene.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.windylee.scene.R;
import com.windylee.scene.entity.Constants;
import com.windylee.scene.entity.Scene;
import com.windylee.scene.utils.ToastUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by windylee on 4/7/17.
 */

public class ModeAdapter extends RecyclerView.Adapter<ModeAdapter.ViewHolder> {

    private List<Scene> mModes;
    private onModeItemClickListener mOnModeItemClickListener;
    private Context mContext;
    private int type;

    public ModeAdapter(Context context, List<Scene> mModes, int type) {
        this.mContext = context;
        this.mModes = mModes;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mode, parent, false);
        ViewHolder viewHolder = new ViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mNameView.setText(mModes.get(position).getSceneName());
        holder.mDescView.setText(mModes.get(position).getSceneDesc());
        holder.openView.setChecked(mModes.get(position).isOpen());
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.openView.toggle();
                boolean isOpen = holder.openView.isChecked();
                mModes.get(position).setOpen(isOpen);
                if (isOpen && type == Constants.NORMAL_CODE) {
                    for (int i = 0; i < mModes.size(); ++i) {
                        if (i != position) mModes.get(i).setOpen(false);
                    }
                    notifyDataSetChanged();
                }
                if (mOnModeItemClickListener != null && isOpen) {
                    ToastUtils.showToast(mContext, "模式已启用");
                    mOnModeItemClickListener.OnModeItemClicked(position, mModes.get(position).getId(), isOpen);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //return mModes.size();
        return mModes.size();
    }

    public void setModeItems(List<Scene> items) {
        if (!mModes.equals(items)) {
            mModes.clear();
            mModes.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setOnModeItemClickListener(onModeItemClickListener mOnModeItemClickListener) {
        this.mOnModeItemClickListener = mOnModeItemClickListener;
    }

    public interface onModeItemClickListener {
        void OnModeItemClicked(int position, int modeId, boolean isOpen);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mNameView;
        TextView mDescView;
        SwitchCompat openView;
        View contentView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameView = (TextView) itemView.findViewById(R.id.mode_name);
            mDescView = (TextView) itemView.findViewById(R.id.mode_desc);
            openView = (SwitchCompat) itemView.findViewById(R.id.mode_open);
            contentView = itemView.findViewById(R.id.item_content);
        }

    }
}
