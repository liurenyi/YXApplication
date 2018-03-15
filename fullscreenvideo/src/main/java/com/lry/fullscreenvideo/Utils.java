package com.lry.fullscreenvideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class Utils {

    public static final String uri = "/mnt/sdcard/龙飞-亲爱的你在哪里.mpg";

    // 获取SharedPreferences的值
    public static String getPrefValues(Context context, String tag, String values) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(tag, values);
    }

    // 设置SharedPreferences的值
    public static void setPrefValues(Context context, String tag, String values) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(tag, values);
        editor.apply();
    }

}
