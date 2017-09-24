package com.example.lenove.zhihunews.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.db.RealmHelper;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.example.lenove.zhihunews.widget.OffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by lenove on 2017/6/4.
 */

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private BaseAdapter mAdapter;
    private List<NewsItem> mData;
    private RealmHelper mRealmHelper;
    private int numsFavorited;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_favorite, container, false);
        mData = new ArrayList<>();
        initRecycylerView(root);
        getDataFromDb();
        getActivity().setTitle(numsFavorited + "条收藏");
        return root;

    }


    private void getDataFromDb() {
        mRealmHelper = new RealmHelper(getActivity());
        RealmResults<NewsItem> listFavorited = mRealmHelper.queryAllFavorited();
        numsFavorited = listFavorited.size();
        Log.d("111", "size" + listFavorited.size());
        for (NewsItem news : listFavorited) {
            NewsItem item = new NewsItem();

            item.setTitle(news.getTitle());
            item.setId(news.getId());
            item.setUrl(news.getUrl());
            item.setFavorited(news.isFavorited());
            Log.d("111", "FF" + item.getTitle());
            mData.add(item);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initRecycylerView(View root) {
        recyclerView = (RecyclerView) root.findViewById(R.id.rv_fav);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BaseAdapter(mData,getActivity());
        final int spacing = getContext().getResources()
                .getDimensionPixelSize(R.dimen.spacing_nano);
        recyclerView.addItemDecoration(new OffsetDecoration(spacing));
        recyclerView.setAdapter(mAdapter);
    }
}
