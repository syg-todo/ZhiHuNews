package com.example.lenove.zhihunews.favorite;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lenove.zhihunews.R;

/**
 * Created by lenove on 2017/6/4.
 */

public class FavoriteActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.frag_favorite);
    }
}
