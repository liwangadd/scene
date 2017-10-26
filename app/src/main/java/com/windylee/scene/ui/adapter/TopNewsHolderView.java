package com.windylee.scene.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.squareup.picasso.Picasso;
import com.windylee.scene.R;
import com.windylee.scene.entity.DailyNews;

/**
 * Created by windylee on 4/12/17.
 */

public class TopNewsHolderView implements Holder<DailyNews.TopStoriesBean> {
    private ImageView mHeaderImageView;
    private TextView mHeaderTextView;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_news_header_item,
                null);
        mHeaderImageView = (ImageView) view.findViewById(R.id.iv_header);
        mHeaderTextView = (TextView) view.findViewById(R.id.tv_header);
        mHeaderImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, DailyNews.TopStoriesBean bean) {
        //ImageUtil.getInstance().displayImage(bean.getImage(), mHeaderImageView);
        Picasso.with(context).load(bean.getImage()).into(mHeaderImageView);
        mHeaderTextView.setText(bean.getTitle());
    }
}
