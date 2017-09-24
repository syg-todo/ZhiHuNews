package com.example.lenove.zhihunews.photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.widget.ZoomImageView;

/**
 * Created by lenove on 2017/5/22.
 */

public class PhotoActivity extends AppCompatActivity {
    private String imageUrl;
    private ZoomImageView imgPhoto;
    private static final String PHOTO = "PHOTO";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_photo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageUrl = getIntent().getStringExtra(PHOTO);
        imgPhoto = (ZoomImageView) findViewById(R.id.photo_img);

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("111","click");
               if (toolbar.getVisibility() == View.VISIBLE){
                   toolbar.setVisibility(View.INVISIBLE);
               }else {
                   toolbar.setVisibility(View.VISIBLE);
               }
            }
        });
       Log.d("111",imageUrl);
        Glide.with(this).load(imageUrl).into(imgPhoto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("111","save");
        Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
