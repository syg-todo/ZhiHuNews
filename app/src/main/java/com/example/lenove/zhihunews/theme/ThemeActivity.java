package com.example.lenove.zhihunews.theme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.util.ActivityUtils;

/**
 * Created by lenove on 2017/5/16.
 */

public class ThemeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTheme);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        ThemeFragment themeFragment = (ThemeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrameTheme);
        if (themeFragment == null){
            themeFragment = ThemeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), themeFragment, R.id.contentFrameTheme);

        }
    }
}
