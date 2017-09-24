package com.example.lenove.zhihunews.entity;

import java.util.List;

/**
 * Created by lenove on 2017/7/21.
 */

public class SectionItem<NewsItem> {
    private String mDate;
    private List<NewsItem> mItems;

    public SectionItem(String date, List<NewsItem> items) {
        mDate = date;
        mItems = items;
    }

    public String getDate() {
        return mDate;
    }

    public NewsItem getmItem(int position) {
        return mItems.get(position);
    }

    public int getCount() {
        return (mItems == null ? 1 : 1 + mItems.size());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof SectionItem){
            return ((SectionItem) obj).getDate().equals(mDate);
        }
        return false;
    }
}
