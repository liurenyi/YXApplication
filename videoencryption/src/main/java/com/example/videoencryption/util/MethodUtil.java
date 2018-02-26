package com.example.videoencryption.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/2/26.
 */

public class MethodUtil {

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void log(String tag, String content, int type) {
        if (type == 1) {
            Log.v(tag, content);
        } else if (type == 2) {
            Log.d(tag, content);
        } else if (type == 3) {
            Log.i(tag, content);
        } else if (type == 4) {
            Log.w(tag, content);
        } else if (type == 5) {
            Log.e(tag, content);
        }
    }
}
