package com.psu.capstonew17.pdxaslapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by thanhhoang on 4/6/17.
 */

public class VideoLayout extends VideoView {
    public VideoLayout(Context context) {
        super(context);
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, 3/4 * widthMeasureSpec);
    }
}
