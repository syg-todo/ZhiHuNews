package com.example.lenove.zhihunews.home;

import android.content.Intent;
import android.graphics.Rect;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenove.zhihunews.widget.BannerAdapter;
import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.db.News;
import com.example.lenove.zhihunews.db.RealmHelper;
import com.example.lenove.zhihunews.entity.NewsBean;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.example.lenove.zhihunews.network.HttpMethod;
import com.example.lenove.zhihunews.util.ActivityUtils;
import com.example.lenove.zhihunews.widget.Banner;
import com.example.lenove.zhihunews.widget.OffsetDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import rx.Subscriber;


/**
 * Created by lenove on 2017/4/25.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, HomeContract.View {
    private HomeContract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<BannerModel> dataBanner = new ArrayList<BannerModel>();
    private List<NewsItem> dataNews;
    private HomeAdapter mAdapter;
    private RealmHelper realmHelper;
    private int currentDate;
    private Date date;
    private int total;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_home, container, false);

        currentDate = getCurrentDate();
        date = new Date();

        realmHelper = new RealmHelper(getActivity());
        initRecyclerView(root);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_home);


        swipeRefreshLayout.setOnRefreshListener(this);
//        getNewsFromDb();
//        getBannerFromDb();
        setHasOptionsMenu(true);
        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_messages:
                break;
            case R.id.action_mode:
                if (item.getTitle().equals("夜间模式")) {
                    item.setTitle("日间模式");
//                    getActivity()getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    item.setTitle("夜间模式");
//                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void initRecyclerView(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);

        dataNews = new ArrayList<NewsItem>();


        mAdapter = new HomeAdapter(dataNews, getActivity(), dataBanner);
        recyclerView.setAdapter(mAdapter);
        final int spacing = getContext().getResources()
                .getDimensionPixelSize(R.dimen.spacing_nano);
        recyclerView.addItemDecoration(new OffsetDecoration(spacing));


        recyclerView.addOnScrollListener(new LoadMoreListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.d("111", "onLoadMore");
                mPresenter.loadMoreNews();

            }

            @Override
            public void onChange(int position) {

                if (position < total) {
                    getActivity().setTitle("今日新闻");
                } else {
                    date = new Date();
                    getActivity().setTitle(getDate(getNewsDate(position, total)));
                }
            }
        });
    }

    private int getNewsDate(int position, int total) {
        Log.d("111", total + "size" + position);
        String title;
        int eachCount = 20;
        return (position - total) / eachCount + 1;

    }


    private void loadMore() {
        //http://news-at.zhihu.com/api/4/news/before/20170613
        currentDate--;
        getBeforeNews(currentDate);
//        NewsItem item = new NewsItem();
//        item.setTitle("hello");
//        item.setUrl("https://pic2.zhimg.com/v2-09b3e633583a3421ce5c4d27413f5439.jpg");
//        item.setFavorited(false);
//        item.setId(9472212);
//        dataNews.add(item);
//        mAdapter.notifyDataSetChanged();
//        Log.d("111", "loadmore");
    }

    private void getBeforeNews(final int currentDate) {
        Subscriber<NewsBean> subscriber = new Subscriber<NewsBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewsBean newsBean) {
                List<NewsBean.StoryBean> list = newsBean.getStories();
                NewsItem newsItem = new NewsItem();
                newsItem.setTitle(currentDate + "年");
                dataNews.add(newsItem);
//                mAdapter.notifyDataSetChanged();
                for (NewsBean.StoryBean news : list) {
                    NewsItem item = new NewsItem();
                    item.setTitle(news.getTitle());
                    item.setFavorited(false);
                    item.setUrl(news.getImages().get(0));
                    item.setId(news.getId());
                    dataNews.add(item);
                    mAdapter.notifyDataSetChanged();
                }
                String title = newsBean.getStories().get(0).getTitle();
                Log.d("111", title);
            }
        };

//        HttpMethod.getInstance().getBeforeNews(subscriber, currentDate);
    }

    private int getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int currentDate = year * 10000 + month * 100 + date;
        return currentDate;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.loadNews();
        mPresenter.loadBanners();
    }


    private void saveIntoDb(List<NewsItem> dbList) {
        //
        Log.d("111", "saveIntoDb");
//        RealmResults<News> dbListNow = realmHelper.queryAll();
        Log.d("111", dbList.size() + "");

        for (NewsItem news : dbList) {

            realmHelper.addNews(news);
        }

    }


//    private void refresh() {
//        getDataFromNet();
//        Toast.makeText(getActivity(),"refresh",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        onRefresh();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNews(List<NewsItem> news) {
        int date = news.get(0).getDate();
        if (date == getCurrentDate()) {

            mAdapter.addSection("今日新闻", news);
        } else {
            mAdapter.addSection(date + "", news);
        }

        total = news.size() + 1;

//        dataNews.add(news.get(0));
//        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void showNewsDetailsUi() {

    }

    @Override
    public void showMarkRead() {

    }

    @Override
    public void showMoreNews(List<NewsItem> news) {
        mAdapter.addSection(getDate(1), news);
    }

    //    private String date(int date){
//        SimpleDateFormat simple = new SimpleDateFormat("MM月dd日 E");
//        //20170801 8月01日 周二
//
//
//    }
    private String getDate(int n) {
        date.setTime(date.getTime() - 60 * 60 * 1000 * 24 * n);
        SimpleDateFormat simple = new SimpleDateFormat("MM月dd日 E");

        return simple.format(date);
    }

    @Override
    public void showBanners(List<BannerModel> bannerModels) {

        for (BannerModel bannerModel : bannerModels) {
            Log.d("111", "showBanner" + bannerModel.getTips());
            dataBanner.add(bannerModel);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
        Log.d("111", "refresh");
        swipeRefreshLayout.setRefreshing(false);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;


        public SpaceItemDecoration(int space) {
            this.mSpace = ActivityUtils.dp2px(getActivity(), space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = mAdapter.getItemCount();
            int pos = parent.getChildAdapterPosition(view);


            outRect.bottom = 0;


            if (pos != (itemCount - 1)) {
                outRect.left = mSpace;
                outRect.right = mSpace;
                outRect.top = mSpace;
            } else {
                outRect.left = 20;
                outRect.right = 20;
                outRect.top = 20;

            }
        }
    }

    private abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        private int previousTotal = 0;
        private boolean loading = true;
        int firstVisibleItem, visibleItemCount, totalItemCount;

        private int currentPage = 1;

        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerOnScrollListener(LinearLayoutManager mLinearLayoutManager) {
            this.mLinearLayoutManager = mLinearLayoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            visibleItemCount = recyclerView.getChildCount();

            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading
                    && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
                currentPage++;
                onLoadMore(currentPage);
                loading = true;
            }

        }

        public abstract void onLoadMore(int currentPage);

    }
}
