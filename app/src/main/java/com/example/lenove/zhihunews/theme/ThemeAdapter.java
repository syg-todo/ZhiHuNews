package com.example.lenove.zhihunews.theme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.entity.ThemeBean;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenove on 2017/5/17.
 */

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ThemeHolder> {
    private ArrayList<ThemeBean.StoriesBean> list;

    public static class ThemeHolder extends RecyclerView.ViewHolder {

        private TextView mItemTitle;
        private ImageView mItemImage;
        private String title;
        private String url;
        private ThemeBean.StoriesBean mStoryBean;

        public ThemeHolder(View itemView) {
            super(itemView);
            mItemTitle = (TextView) itemView.findViewById(R.id.home_item_title);
            mItemImage = (ImageView) itemView.findViewById(R.id.home_item_img);

        }

        public void bindHolder(ThemeBean.StoriesBean storyBean) {


            this.mStoryBean = storyBean;
            title = storyBean.getTitle();

            mItemTitle.setText(title);
            mItemImage.setImageResource(R.mipmap.ic_launcher);
            if (storyBean.getImages() != null){
                url = storyBean.getImages().get(0);
                Picasso.with(mItemImage.getContext()).load(url).into(mItemImage);
            }else {
                mItemImage.setVisibility(View.GONE);
            }

        }
    }

    public ThemeAdapter(ArrayList<ThemeBean.StoriesBean> storiesBean) {
        list = storiesBean;
    }

    @Override
    public ThemeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        return new ThemeHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ThemeHolder holder, int position) {
        ThemeBean.StoriesBean storyBean = list.get(position);
        holder.bindHolder(storyBean);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}
