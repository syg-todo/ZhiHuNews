package com.example.lenove.zhihunews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lenove.zhihunews.home.MainActivity;

/**
 * Created by lenove on 2017/6/9.
 */

public class SplashActivity extends AppCompatActivity{
    private ImageView ivSplash;
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private final String IMAGE_URL = "https://pic1.zhimg.com/v2-63998366b2baa3ec6805735e9a99ab5c.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
        Glide.with(this).load(IMAGE_URL).into(ivSplash);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
               toMainActivity();
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);


    }

    private void toMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void getImageUrl(){

    }
}
