package com.example.lenove.zhihunews.data;

import com.example.lenove.zhihunews.entity.NewsBean;
import com.example.lenove.zhihunews.entity.NewsItem;

import java.util.List;

import rx.Observable;

/**
 * Created by lenove on 2017/7/7.
 */

public interface NewsDataSource {
    Observable<List<NewsItem>> getNewsFromNet();

    Observable<List<NewsItem>> getBeforeNews(int date);
    Observable<NewsBean> getNews(int id);

    void saveNews(NewsItem newsItem);
}
