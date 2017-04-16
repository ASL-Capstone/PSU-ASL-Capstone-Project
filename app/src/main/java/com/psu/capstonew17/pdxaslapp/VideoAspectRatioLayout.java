package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by thanhhoang on 4/5/17.
 */

/**
 * This custom layout extended LinearLayout, is placeholder for
 * VideoView, the respect ratio is set to 4:3
 */
public class VideoAspectRatioLayout extends LinearLayout {
    public VideoAspectRatioLayout(Context context) {
        super(context);
    }

    public VideoAspectRatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoAspectRatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoAspectRatioLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = this.getMeasuredWidth();
        int height = (int) (width/4)*3;
        setMeasuredDimension(width, height);
    }
}