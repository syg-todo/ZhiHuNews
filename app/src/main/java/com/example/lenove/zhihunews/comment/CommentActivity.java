package com.example.lenove.zhihunews.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.lenove.zhihunews.R;
import com.example.lenove.zhihunews.util.ActivityUtils;

/**
 * Created by lenove on 2017/5/5.
 */

public class CommentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        CommentFragment commentFragment = (CommentFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrameComment);
        if (commentFragment == null) {
            Log.d("111","null");
            commentFragment = CommentFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), commentFragment, R.id.contentFrameComment);
        }

    }
}
