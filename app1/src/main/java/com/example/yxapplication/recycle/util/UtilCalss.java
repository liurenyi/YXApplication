package com.example.yxapplication.recycle.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/1/19.
 */

public class UtilCalss {

    public static final String TAG = "UtilCalss";
    private static WindowManager windowManager;

    public static int getWidth(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    public static int getHeight(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }
}
