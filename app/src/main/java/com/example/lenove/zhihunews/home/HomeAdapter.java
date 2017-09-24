package com.example.lenove.zhihunews.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.detail.DetailActivity;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.example.lenove.zhihunews.entity.SectionItem;
import com.example.lenove.zhihunews.widget.Banner;
import com.example.lenove.zhihunews.widget.BannerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenove on 2017/8/14.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private static final int TYPE_BANNER = 0;
    private static final int TYPE_HEADER = 1;
    public static final int TYPE_NORMAL = 2;
    private static final String DETAIL_ID = "DETAIL";
    private static final String IMAGE = "IMAGE";
    private static final String TITLE = "TITLE";
    private Activity activity;
    private List<SectionItem<NewsItem>> mSections;
    private SparseArray<SectionItem<NewsItem>> mKeyedSections;
    private List<NewsItem> newsList = new ArrayList<>();
    private View mHeaderView;
    private View mBannerView;
    private View mItemView;
    private List<BannerModel> dataBanner = new ArrayList<BannerModel>();
    private BannerAdapter bannerAdapter;

    public HomeAdapter(List<NewsItem> news, Activity activity,List<BannerModel> banners) {
        newsList = news;
        mSections = new ArrayList<SectionItem<NewsItem>>();
        mKeyedSections = new SparseArray<SectionItem<NewsItem>>();
        dataBanner = banners;
        this.activity = activity;
    }

    public void addSection(String title, List<NewsItem> items) {
        SectionItem<NewsItem> sectionItem = new SectionItem<NewsItem>(title, items);
        int currentIndex = mSections.indexOf(sectionItem);
        if (currentIndex >= 0) {
            mSections.remove(sectionItem);
            mSections.add(currentIndex, sectionItem);
        } else {
            mSections.add(sectionItem);
        }

        reorderSections();
        notifyDataSetChanged();
    }

    private void reorderSections() {
        mKeyedSections.clear();
        int startPosition = 1;
        for (SectionItem<NewsItem> item : mSections) {
            mKeyedSections.put(startPosition, item);
            startPosition += item.getCount();
        }
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                mBannerView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banner, parent, false);
                return new HomeHolder(mBannerView);

            case TYPE_HEADER:
                mHeaderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
                return new HomeHolder(mHeaderView);
            case TYPE_NORMAL:
                mItemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);

              return new HomeHolder(mItemView);
            default:
                Log.d("error", "viewholder is null");
                return null;
        }

    }




    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER){
            holder.bindBanner(dataBanner);
        }else if (getItemViewType(position) == TYPE_HEADER){
            SectionItem<NewsItem> item = mKeyedSections.get(position);
            holder.bindHeader(item.getDate());
        }else {
            NewsItem news = findSectionItemAtPosition(position);

            holder.bindItem(news);
        }

    }



    private NewsItem findSectionItemAtPosition(int position) {
        int firstIndex, lastIndex;
        for (int i = 0; i < mKeyedSections.size(); i++) {
            firstIndex = mKeyedSections.keyAt(i);
            lastIndex = firstIndex + mKeyedSections.valueAt(i).getCount();
            if (position >= firstIndex && position < lastIndex) {
                int sectionPosition = position - firstIndex - 1;
                return mKeyedSections.valueAt(i).getmItem(sectionPosition);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (isHeaderAtPostion(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }

    }

    private boolean isHeaderAtPostion(int position) {
        for (int i = 0; i < mKeyedSections.size(); i++) {
            if (position == mKeyedSections.keyAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (SectionItem<NewsItem> item : mSections) {
            count += item.getCount();
        }
        return count + 1;
    }

    public class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mItemTitle, mHeaderTitle;
        private ImageView mItemImage;
        private String title;
        private String url;
        private int id;
        private Banner banner;
        private static final String DETAIL = "DETAIL";
        public HomeHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                mHeaderTitle = (TextView) itemView.findViewById(R.id.header);

                return;
            } else if (itemView == mBannerView){
                banner = (Banner) itemView.findViewById(R.id.banner);
            }else {
                mItemTitle = (TextView) itemView.findViewById(R.id.home_item_title);
                mItemImage = (ImageView) itemView.findViewById(R.id.home_item_img);
                itemView.setOnClickListener(this);
            }

        }

        public void bindHeader(String title){

            mHeaderTitle.setText(title);
        }

        public void bindItem(NewsItem item){
            url = item.getUrl();
            id = item.getId();
            title = item.getTitle();
            Glide.with(activity).load(url).into(mItemImage);

            mItemTitle.setText(title);
        }

        public void bindBanner(List<BannerModel> dataBanner){
            Log.d("111","bindBanners");
            Log.d("111","bindBanner:"+dataBanner.size());
            if (dataBanner.size()!=0){
                bannerAdapter = new BannerAdapter<BannerModel>(dataBanner) {
                    @Override
                    public void onClick(View v) {

                    }

                    @Override
                    protected void bindTips(TextView tv, BannerModel bannerModel) {
                        tv.setText(bannerModel.getTips());
                    }

                    @Override
                    public void bindImage(ImageView imageView, BannerModel bannerModel) {
                        Glide.with(activity)
                                .load(bannerModel.getImageUrl())

                                .into(imageView);
                    }

                    @Override
                    public void onClick(BannerModel bannerModel) {
                        Log.d("111","onClick"+bannerModel.getTips());
                    }

                };
               banner.setBannerAdapter(bannerAdapter);
               banner.notifyDataHasChanged();
            }
        }


        @Override
        public void onClick(View v) {


            Context context = itemView.getContext();
            Intent showDetail = new Intent(context, DetailActivity.class);
            showDetail.putExtra("image", url);
            showDetail.putExtra("title", title);
            Log.d("111", "HA url" + url);
            showDetail.putExtra(DETAIL, id);
            Pair<View, String> pair = Pair.create(v.findViewById(R.id.home_item_img), "image");
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pair);
            ActivityCompat.startActivity(context, showDetail, options.toBundle());
        }
    }
}
