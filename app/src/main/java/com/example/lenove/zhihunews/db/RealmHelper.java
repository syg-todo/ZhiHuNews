package com.example.lenove.zhihunews.db;

import android.content.Context;
import android.util.Log;

import com.example.lenove.zhihunews.entity.NewsItem;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by lenove on 2017/6/2.
 */

public class RealmHelper {
    public static final String DB_NAME = "zhihu.realm";
    private Realm mRealm;


    public RealmHelper(Context context) {

        mRealm = Realm.getDefaultInstance();
    }

    public void addNews(NewsItem news) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(news);
        mRealm.commitTransaction();
//        Log.d("111","RealmHelper save success");
    }

    public RealmResults<NewsItem> queryAll(String type) {
        return mRealm.where(NewsItem.class).equalTo("type", type).findAll();
    }

    public RealmResults<NewsItem> queryWithDate(int date){
        return mRealm.where(NewsItem.class).equalTo("date",date).findAll();
    }
    public RealmResults<NewsItem> queryAllFavorited() {
        return mRealm.where(NewsItem.class).equalTo("isFavorited", true).findAll();
    }

    public void setIsFavorited(final int id, final boolean isFavorited) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                NewsItem news = realm.where(NewsItem.class).equalTo("id", id).findFirst();
                if (news != null) {
                    news.setFavorited(isFavorited);
                }
            }
        });
    }

    public boolean getFavorited(final int id) {
        boolean isFavorited = false;

        NewsItem news = mRealm.where(NewsItem.class).equalTo("id", id).findFirst();
        if (news != null) {
            isFavorited = news.isFavorited();
        }
        Log.d("111","getFavorited"+isFavorited);
        return isFavorited;
    }

}
