package com.example.lenove.zhihunews.home;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lenove.zhihunews.data.NewsRepository;
import com.example.lenove.zhihunews.entity.NewsItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lenove on 2017/7/6.
 */

public class HomePresenter implements HomeContract.Presenter {
    @NonNull
    private final HomeContract.View mHomeView;
    private NewsRepository mNewsRepository;
    private int currentDate;
    private Date date;
    public HomePresenter(NewsRepository newsRepository, HomeContract.View homeView) {
        mHomeView = homeView;
        mNewsRepository = newsRepository;
        mHomeView.setPresenter(this);
        currentDate = getCurrentDate();
        date = new Date();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void loadBanners(){
        Log.d("111","loadBanner");
        mNewsRepository.getBannersFromNet()
                .flatMap(new Func1<List<BannerModel>, Observable<BannerModel>>() {
                    @Override
                    public Observable<BannerModel> call(List<BannerModel> bannerModels) {
                        return Observable.from(bannerModels);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BannerModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("111","banner error");
                    }

                    @Override
                    public void onNext(List<BannerModel> bannerModels) {
                        Log.d("111","onNext"+bannerModels.get(0).getImageUrl());
                        mHomeView.showBanners(bannerModels);
                    }
                });
    }

    @Override
    public void refresh(){
        test(mNewsRepository.getNewsFromNet());


    }

    private void test(Observable<List<NewsItem>> observable){
        observable.flatMap(new Func1<List<NewsItem>, Observable<NewsItem>>() {
            @Override
            public Observable<NewsItem> call(List<NewsItem> newsItems) {

                return Observable.from(newsItems);
            }
        })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NewsItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("111", "error");
                    }

                    @Override
                    public void onNext(List<NewsItem> newsItems) {


                        mHomeView.showNews(newsItems);
                    }
                });

    }
    @Override
    public void loadNews() {
        Log.d("111", "HP loadNews");
        test(mNewsRepository.getNewsFromDb(getCurrentDate()));

    }

    @Override
    public void loadMoreNews() {

        Log.d("111","loadMoreNews:"+currentDate);
        mNewsRepository.getBeforeNews(currentDate)
                .flatMap(new Func1<List<NewsItem>, Observable<NewsItem>>() {
                    @Override
                    public Observable<NewsItem> call(List<NewsItem> newsItems) {
                        return Observable.from(newsItems);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<NewsItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<NewsItem> newsItems) {
                        mHomeView.showMoreNews(newsItems);
                    }
                });

        currentDate--;
    }
    private int getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int currentDate = year * 10000 + month * 100 + date;
        Log.d("111","getCurrentDate"+currentDate);
        return currentDate;
    }
}
