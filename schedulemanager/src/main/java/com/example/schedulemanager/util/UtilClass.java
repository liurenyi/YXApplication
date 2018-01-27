package com.example.schedulemanager.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.schedulemanager.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/22.
 */

public class UtilClass {

    private static Date date = new Date();

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     *
     * @param string
     * @return
     */
    public static String getFormatString(String string) {
        String str = "";
        if (string != null) {
            Pattern pattern = Pattern.compile("\\s|\n|\r|\t");//去掉
            Matcher matcher = pattern.matcher(string);
            str = matcher.replaceAll("");
        }
        return str;
    }


    /**
     * 定义状态栏的颜色
     *
     * @param activity
     */
    public static void initSystemBar(Activity activity) {
        /**
         * 4.4版本以上开启
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);
        //tintManager.setNavigationBarTintResource(R.color.colorWhite); // 无颜色出来,自定义的颜色
        //tintManager.setTintColor(R.color.); // 无颜色出来 ???
        //tintManager.setStatusBarTintResource(R.color.colorWhite); // 无颜色出来 ??? 设置为白色有毒，不显示，还是那个阴影
        tintManager.setTintColor(Color.parseColor("#00FFccFF"));
        //tintManager.setStatusBarAlpha(255);// 0全部透明
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
