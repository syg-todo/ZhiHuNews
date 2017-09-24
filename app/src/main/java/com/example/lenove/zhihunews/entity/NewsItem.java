package com.example.lenove.zhihunews.entity;


import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by lenove on 2017/4/26.
 */

public class NewsItem extends RealmObject implements Serializable{
    private String title;
    private String url;
    private int id;
    private boolean isFavorited;
    private String type;
    private int date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    //    public NewsItem(String title, String url, int id) {
//        this.title = title;
//        this.url = url;
//        this.id = id;
//    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }
}
