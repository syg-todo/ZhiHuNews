package com.example.lenove.zhihunews.network;

import com.example.lenove.zhihunews.entity.NewsBean;
import com.example.lenove.zhihunews.entity.NewsItem;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenove on 2017/5/20.
 */

public class HttpMethod {
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private NewsService newsService;

    //构造方法私有
    private HttpMethod() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        newsService = retrofit.create(NewsService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethod INSTANCE = new HttpMethod();
    }

    //获取单例
    public static HttpMethod getInstance(){
        return SingletonHolder.INSTANCE;
    }


    public Observable<NewsBean> getHomeNews(String type){
      return newsService.getHomeNews(type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public void getNews(){

    }
    public Observable<NewsBean> getBeforeNews(int date){
        return newsService.getBeforeNews(date)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
