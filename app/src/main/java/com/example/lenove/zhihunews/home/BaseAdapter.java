package com.example.lenove.zhihunews.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.detail.DetailActivity;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lenove on 2017/4/26.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.HomeHolder> {

    private Activity activity;
    private List<NewsItem> newsList;
    private View mHeaderView;


    public void replaceData(List<NewsItem> news) {
        setList(news);
        notifyDataSetChanged();
    }
    public void addData(String date,List<NewsItem> news){
        Log.d("111","adapter size:"+getItemCount());

        for (NewsItem newsItem :news){
            newsList.add(newsItem);
        }
        notifyDataSetChanged();
    }

    private void setList(List<NewsItem> news) {
        newsList = news;
    }


    public class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemTitle;
        private ImageView mItemImage;
        private String title;
        private String url;
        private int id;
        private NewsItem mNewsItem;

        private static final String DETAIL = "DETAIL";

        public HomeHolder(View itemView) {
            super(itemView);

                mItemTitle = (TextView) itemView.findViewById(R.id.home_item_title);
                mItemImage = (ImageView) itemView.findViewById(R.id.home_item_img);
                itemView.setOnClickListener(this);

        }

        public void bindHolder(NewsItem newsItem) {

            this.mNewsItem = newsItem;
            title = newsItem.getTitle();
            url = newsItem.getUrl();
            id = newsItem.getId();
            mItemTitle.setText(title);
            Picasso.with(mItemImage.getContext()).load(url).into(mItemImage);
        }

        @Override
        public void onClick(View v) {

            Context context = itemView.getContext();
            Intent showDetail = new Intent(context, DetailActivity.class);
            showDetail.putExtra("image", url);
            showDetail.putExtra("title", title);
            showDetail.putExtra(DETAIL, id);
            Pair<View, String> pair = Pair.create(v.findViewById(R.id.home_item_img), "image");
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pair);
            ActivityCompat.startActivity(context, showDetail, options.toBundle());
            mItemTitle.setTextColor(Color.GRAY);
        }
    }

    public BaseAdapter(List<NewsItem> news, Activity activity) {
        newsList = news;
        this.activity = activity;
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
            return new HomeHolder(layout);

    }

    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {
                NewsItem news = newsList.get(position);
                holder.bindHolder(news);

    }



    @Override
    public int getItemCount() {

        return newsList.size();
    }
}
