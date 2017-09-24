package com.example.lenove.zhihunews.theme;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.entity.CircleImageView;
import com.example.lenove.zhihunews.entity.ThemeBean;
import com.example.lenove.zhihunews.network.NewsService;
import com.example.lenove.zhihunews.util.ActivityUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenove on 2017/5/16.
 */

public class ThemeFragment extends Fragment {
    private static final String THEME = "THEME";

    private ImageView imgTheme;
    private TextView txtTitle;
    private CircleImageView imgEditor;
    private String themeId;
    private RecyclerView rvTheme;
    private ThemeAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;



    public static ThemeFragment newInstance(){
        return new ThemeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_theme,container,false);
        imgTheme = (ImageView) root.findViewById(R.id.theme_img_main);
        txtTitle = (TextView) root.findViewById(R.id.theme_txt_title);
        imgEditor = (CircleImageView) root.findViewById(R.id.theme_img_editor);
        rvTheme = (RecyclerView) root.findViewById(R.id.recyclerview_theme);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        rvTheme.setLayoutManager(mLinearLayoutManager);
        rvTheme.addItemDecoration(new SpaceItemDecoration(10));


        themeId = getTag().split("-")[0];
        getActivity().setTitle(getTag().split("-")[1]);
        Log.d("111",themeId);
        getData(themeId);
        return root;
    }
    private void getData( String id) {
        String baseUrl = "http://news-at.zhihu.com/api/4/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        NewsService newsService = retrofit.create(NewsService.class);

        newsService.getTheme("theme",id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ThemeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ThemeBean themeBean) {
                        String url = themeBean.getBackground();
                        Picasso.with(getActivity()).load(url).into(imgTheme);
                        txtTitle.setText(themeBean.getDescription());
                        Picasso.with(getActivity()).load(themeBean.getEditors().get(0).getAvatar()).into(imgEditor);
                        mAdapter = new ThemeAdapter(themeBean.getStories());
                        rvTheme.setAdapter(mAdapter);
                    }
                });
        //TODO

    }
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace ;


        public SpaceItemDecoration(int space) {
            this.mSpace = ActivityUtils.dp2px(getActivity(),space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = mAdapter.getItemCount();
            int pos = parent.getChildAdapterPosition(view);


            outRect.bottom = 0;


            if (pos != (itemCount -1)) {
                outRect.left = mSpace;
                outRect.right = mSpace;
                outRect.top = mSpace;
            } else {
                outRect.left = 0;
                outRect.right = 0;
                outRect.top = 0;

            }
        }
    }


}
