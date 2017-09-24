package com.example.lenove.zhihunews;

import android.app.Application;

import com.example.lenove.zhihunews.db.RealmHelper;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lenove on 2017/6/1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder()
                .name(RealmHelper.DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
