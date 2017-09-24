package com.example.lenove.zhihunews.data;

import android.content.Context;
import android.util.Log;

import com.example.lenove.zhihunews.db.News;
import com.example.lenove.zhihunews.db.RealmHelper;
import com.example.lenove.zhihunews.entity.NewsBean;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.example.lenove.zhihunews.home.BannerModel;
import com.example.lenove.zhihunews.network.HttpMethod;
import com.example.lenove.zhihunews.network.NewsService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lenove on 2017/7/7.
 */


public class NewsRepository implements NewsDataSource {
    private static NewsRepository INSTANCE = null;
    private static Context mContext;
    private static RealmHelper realmHelper;
    Map<String, NewsItem> mCachedNews;
    boolean mCacheIsDirty = false;

    public NewsRepository() {


    }

    public static NewsRepository getInstance(Context context) {
        realmHelper = new RealmHelper(context);
        if (INSTANCE == null) {
            INSTANCE = new NewsRepository();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public Observable<List<BannerModel>> getBannersFromNet(){
        return HttpMethod.getInstance().getHomeNews("news")
                .flatMap(new Func1<NewsBean, Observable<BannerModel>>() {
                    @Override
                    public Observable<BannerModel> call(NewsBean newsBean) {
                        return Observable.from(getBannerItems(newsBean));
                    }
                }).toList();
    }
    public Observable<List<NewsItem>> getNewsFromDb(int date){
        RealmResults<NewsItem> newsList = realmHelper.queryWithDate(date);
        if (newsList.size() == 0) {
            return getNewsFromNet();
        } else {
            List<NewsItem> list = new ArrayList<NewsItem>();
            for (NewsItem news : newsList) {
                list.add(news);
            }
            return Observable.from(list).toList();
        }
    }

    @Override
    public Observable<List<NewsItem>> getBeforeNews(int date){
        return HttpMethod.getInstance().getBeforeNews(date)
                .flatMap(new Func1<NewsBean, Observable<NewsItem>>() {
                    @Override
                    public Observable<NewsItem> call(NewsBean newsBean) {
                        return Observable.from(getNewsItems(newsBean));
                    }
                }).toList();
    }
    @Override
    public Observable<List<NewsItem>> getNewsFromNet() {
        Log.d("111","NR getNewsFromNet");
        return HttpMethod.getInstance().getHomeNews("news")
                .flatMap(new Func1<NewsBean, Observable<NewsItem>>() {
                    @Override
                    public Observable<NewsItem> call(NewsBean newsBean) {
                        return Observable.from(getNewsItems(newsBean))
                                .doOnNext(new Action1<NewsItem>() {
                                    @Override
                                    public void call(NewsItem newsItem) {
                                        saveNews(newsItem);
                                    }
                                });

                    }
                }).toList();

    }

    private List<BannerModel> getBannerItems(NewsBean newsBean){
        List<NewsBean.TopBean> list = newsBean.getTop_stories();
        List<BannerModel> items = new ArrayList<>();
        for (NewsBean.TopBean topBean : list){
            BannerModel bannerModel = new BannerModel(topBean.getImages(),topBean.getTitle());
            items.add(bannerModel);
        }
        return items;
    }
    private List<NewsItem> getNewsItems(NewsBean newsBean) {
        int date = newsBean.getDate();
        List<NewsBean.StoryBean> list = newsBean.getStories();
        List<NewsItem> items = new ArrayList<>();
        for (int i = 0;i<list.size();i++){
            String title,url;
            int id;
            NewsItem item  = new NewsItem();
            item.setDate(date);
            title = list.get(i).getTitle();
            item.setTitle(title);
            item.setFavorited(false);
            url = list.get(i).getImages().get(0);
            item.setUrl(url);
            id = list.get(i).getId();
            item.setId(id);
            item.setType("news");
            items.add(item);
        }
        return items;
    }

    private void getDataFromNet() {

        Log.d("111", "getDataFromNet");


    }


    private Observable<List<NewsItem>> getAndCacheLocalTasks() {
        return null;
    }

    private Observable<List<NewsItem>> getAndSaveRemoteNews() {
        return null;
    }

    @Override
    public Observable<NewsBean> getNews(int id) {
        return null;
    }

    @Override
    public void saveNews(NewsItem newsItem) {
//        Log.d("111","saveNews");
        realmHelper.addNews(newsItem);
    }


}
