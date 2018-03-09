package com.example.songmachine.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MethodUtil {

    /**
     * toast分装类,减少每次写的麻烦
     *
     * @param context context
     * @param content 吐司的内容
     */
    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

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
