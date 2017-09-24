package com.example.lenove.zhihunews.network;

import com.example.lenove.zhihunews.entity.NewsBean;
import com.example.lenove.zhihunews.entity.CommentBean;
import com.example.lenove.zhihunews.entity.ContentBean;
import com.example.lenove.zhihunews.entity.ExtraBean;
import com.example.lenove.zhihunews.entity.ThemeBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by lenove on 2017/4/25.
 */

public interface NewsService {
    @GET("{type}/latest")
    Observable<NewsBean> getHomeNews(@Path("type") String type);

    @GET("{type}/{id}")
    Observable<ContentBean> getContent(@Path("type") String type,@Path("id") int id);

    @GET("{type}/{id}/{length}")
    Observable<CommentBean> getComment(@Path("type") String type,@Path("id") int id,@Path("length") String length);

    @GET("{type}/{id}")
    Observable<ThemeBean> getTheme(@Path("type") String type,@Path("id") String id);

    @GET("{type}/{id}")
    Observable<ExtraBean> getExtra(@Path("type") String type, @Path("id") int id);

    @GET("news/before/{date}")
    Observable<NewsBean> getBeforeNews(@Path("date") int date);

}
