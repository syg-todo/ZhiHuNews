package com.example.lenove.zhihunews.db;

import org.litepal.crud.DataSupport;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lenove on 2017/5/27.
 */

public class News extends RealmObject {
//    @PrimaryKey
    private int newsId;
    private String title;
    private String imageUrl;
    private boolean isFavorited;
    private String type;
    private int date;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
