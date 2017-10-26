package com.windylee.scene.service;

import com.windylee.scene.entity.DailyNews;
import com.windylee.scene.entity.HotNews;
import com.windylee.scene.entity.NewsDetails;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by windylee on 4/13/17.
 */

public interface NewsService {

    @GET(API.ZHIHU_HOT_NEWS)
    Observable<HotNews> getHotNews();

    @GET(API.ZHIHU_NEWS_FOUR + "/news/{date}")
    Observable<DailyNews> getDailyNews(@Path("date") String date);

    @GET(API.ZHIHU_NEWS_FOUR + "/news/before/{date}")
    Observable<DailyNews> getMoreDailyNews(@Path("date") String date);

    @GET("{path}/news/{id}")
    Observable<NewsDetails> getDetailNews(@Path("path") String path, @Path("id") String id);

}
