package com.windylee.scene.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.windylee.scene.R;
import com.windylee.scene.SceneFactory;
import com.windylee.scene.application.APP;
import com.windylee.scene.entity.NewsDetails;
import com.windylee.scene.utils.CacheUtil;
import com.windylee.scene.utils.NetUtils;
import com.windylee.scene.utils.ToastUtils;
import com.windylee.scene.widget.RevealBackgroundView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by windylee on 4/14/17.
 */

public class CollectDetailsActivity extends AppCompatActivity implements RevealBackgroundView.OnStateChangeListener, Toolbar.OnMenuItemClickListener {

    public static final String URL = "url";
    public static final String PATH = "path";
    public static final String LOCATION = "location";
    public int[] mLocation;
    private NewsDetails newDetails;

    @Bind(R.id.rbv_content)
    RevealBackgroundView mContentRevealBackgroundView;
    @Bind(R.id.iv_title)
    ImageView mTitleImageView;
    @Bind(R.id.ctl_title)
    CollapsingToolbarLayout mTitleCollapsingToolbarLayout;
    @Bind(R.id.wb_content)
    WebView mContentWebView;
    @Bind(R.id.nsv_content)
    NestedScrollView mContentNestedScrollView;
    @Bind(R.id.abl_content)
    AppBarLayout mcontentAppBarLayout;
    @Bind(R.id.tb_title)
    Toolbar mTitleToolbar;

    public static Intent newTopStoriesIntent(BaseActivity activity, NewsDetails detail, int[] clickLocation) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("newDetail", detail);
        bundle.putIntArray(LOCATION, clickLocation);
        Intent intent = new Intent(activity, CollectDetailsActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        setSupportActionBar(mTitleToolbar);
        mTitleToolbar.setNavigationIcon(R.mipmap.icon_arrow_back);
        mTitleToolbar.setOnMenuItemClickListener(this);

        WebSettings settings = mContentWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);

        parseIntent();
        initAppBarLayout();
        loadWebView();

        mContentRevealBackgroundView.setOnStateChangeListener(this);
        if (mLocation == null || savedInstanceState != null) {
            mContentRevealBackgroundView.setToFinishedFrame();
        } else {
            mContentRevealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mContentRevealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mContentRevealBackgroundView.startFromLocation(mLocation);
                    return true;
                }
            });
        }
    }

    private void parseIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        newDetails = (NewsDetails) bundle.getSerializable("newDetail");
        mLocation = bundle.getIntArray(LOCATION);
    }


    private void initAppBarLayout() {
        Picasso.with(this).load(newDetails.getImage()).into(mTitleImageView);
        mTitleCollapsingToolbarLayout.setTitle(newDetails.getTitle());
    }

    private void loadWebView() {
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news\" " +
                "type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newDetails.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mContentWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContentWebView != null) {
            mContentWebView.removeAllViews();
            mContentWebView.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mContentWebView != null) {
            mContentWebView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mContentWebView != null) {
            mContentWebView.onResume();
        }
    }

    @Override
    public void onStateChange(int state) {
        if (state == RevealBackgroundView.STATE_FINISHED) {
            mContentNestedScrollView.setVisibility(View.VISIBLE);
            mcontentAppBarLayout.setVisibility(View.VISIBLE);
        } else {
            mContentNestedScrollView.setVisibility(View.GONE);
            mcontentAppBarLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_collect_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.news_collect) {
            APP.mDb.delete(newDetails);
            item.setIcon(R.drawable.ic_uncollect);
            ToastUtils.showToast(this,"该收藏已取消");
            finish();
        }
        return false;
    }

}
