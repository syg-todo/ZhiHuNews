package com.example.lenove.zhihunews.home;

import com.example.lenove.zhihunews.BasePresenter;
import com.example.lenove.zhihunews.BaseView;
import com.example.lenove.zhihunews.entity.NewsItem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lenove on 2017/7/6.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void showNews(List<NewsItem> news);
        void showNewsDetailsUi();

        void showMarkRead();

        void showMoreNews(List<NewsItem> news);

        void showBanners(List<BannerModel> bannerModels);
    }

    interface Presenter extends BasePresenter {
        void loadNews();
        void loadMoreNews();
        void loadBanners();
        void refresh();
    }
}
