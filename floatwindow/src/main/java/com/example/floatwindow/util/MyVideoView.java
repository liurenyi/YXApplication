package com.example.floatwindow.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Administrator on 2018/1/16.
 * 重新自定义VideoView，重新定义其宽高，使其充满屏幕。
 */

public class MyVideoView extends VideoView {

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
