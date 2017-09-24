package com.example.lenove.zhihunews.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.comment.CommentActivity;
import com.example.lenove.zhihunews.entity.ContentBean;
import com.example.lenove.zhihunews.entity.NewsItem;
import com.example.lenove.zhihunews.network.NewsService;
import com.example.lenove.zhihunews.photo.PhotoActivity;
import com.example.lenove.zhihunews.util.ActivityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lenove on 2017/4/28.
 */

public class DetailActivity extends AppCompatActivity {

//    private NewsItem newsItem;
//    private WebView webView;
//    private ImageView imageView;
    private static final String DETAIL = "DETAIL";
    private static final String COMMENT = "COMMENT";
    private int mId;
//    private View mToolbarBack;
    public static void start(Context context, ActivityOptionsCompat options,NewsItem newsItem){
        Intent starter = getStartIntent(context,newsItem);
        ActivityCompat.startActivity(context, starter, options.toBundle());
    }


    @NonNull
    static Intent getStartIntent(Context context,NewsItem newItem) {
        Intent starter = new Intent(context,DetailActivity.class);
        starter.putExtra(DETAIL,newItem);
        return starter;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mId = getIntent().getIntExtra(DETAIL,0);
//        mT = (TextView) findViewById(R.id.detail_test);
//        mToolbarBack = findViewById(R.id.back);
//        mToolbarBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


//        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_back);
//        ab.setDisplayHomeAsUpEnabled(true);



        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrameDetail);
        if (detailFragment == null){
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), detailFragment, R.id.contentFrameDetail);
        }


//        newsItem = (NewsItem) getIntent().getSerializableExtra(DETAIL);
//        List<Fragment> fragments=new ArrayList<Fragment>();
//        fragments.add(detailFragment);
//        fragments.add(detailFragment);
//        FragAdapter adapter=new FragAdapter(getSupportFragmentManager(), fragments);
//        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(adapter);


    }



    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_detail,menu);
//        MenuItem menuItem = menu.findItem(R.id.detail_comment);
//        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent CommentIntent = new Intent(DetailActivity.this, CommentActivity.class);
//                CommentIntent.putExtra(COMMENT,mId);
//                startActivity(CommentIntent);
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.detail_comment).getActionView();
//
//        return super.onPrepareOptionsMenu(menu);
//    }








}
