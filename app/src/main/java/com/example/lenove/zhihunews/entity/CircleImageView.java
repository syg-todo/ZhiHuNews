package com.example.lenove.zhihunews.entity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.example.lenove.zhihunews.R;
import com.google.gson.TypeAdapter;

/**
 * Created by lenove on 2017/5/17.
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

   private Bitmap mRawBitmap;
    private int mRadius;
    private Paint mPaint;
    private int mWidth;
    private BitmapShader mShader;
    private Matrix mMatrix = new Matrix();

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = Math.min(getWidth(), getHeight());
        mRadius = mWidth / 2;
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
     protected void onDraw(Canvas canvas) {
//        Bitmap rawBitmap = getBitmap(getDrawable());
//                if (rawBitmap != null){
//            int viewWidth = getWidth();
//                       int viewHeight = getHeight();
//                       int viewMinSize = Math.min(viewWidth, viewHeight);
//                       float dstWidth = viewMinSize;
//                       float dstHeight = viewMinSize;
//                       if (mShader == null || !rawBitmap.equals(mRawBitmap)){
//                          mRawBitmap = rawBitmap;
//                          mShader = new BitmapShader(mRawBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//                      }
//                  if (mShader != null){
//                          mMatrix.setScale(dstWidth / rawBitmap.getWidth(), dstHeight / rawBitmap.getHeight());
//                          mShader.setLocalMatrix(mMatrix);
//                       }
//            mPaint.setShader(mShader);
//                       float radius = viewMinSize / 2.0f;
//                       canvas.drawCircle(radius, radius, radius, mPaint);
//                   } else {
//                       super.onDraw(canvas);
//                   }
    }

             private Bitmap getBitmap(Drawable drawable){
                 if (drawable instanceof BitmapDrawable){
                         return ((BitmapDrawable)drawable).getBitmap();
                     } else if (drawable instanceof ColorDrawable){
                         Rect rect = drawable.getBounds();
                         int width = rect.right - rect.left;
                         int height = rect.bottom - rect.top;
                         int color = ((ColorDrawable)drawable).getColor();
                         Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                         Canvas canvas = new Canvas(bitmap);
                         canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
                         return bitmap;
                     } else {
                         return null;
        }    }

}
