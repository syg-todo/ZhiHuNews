package com.example.lenove.zhihunews.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.lenove.zhihunews.R;

/**
 * Created by lenove on 2017/7/6.
 */

public class OffsetDecoration extends RecyclerView.ItemDecoration {

    private final int mOffset;

    public OffsetDecoration(int offset) {
        mOffset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.left = mOffset;
        outRect.right = mOffset;
        outRect.bottom = mOffset/2;
        outRect.top = mOffset/2;
    }
}
