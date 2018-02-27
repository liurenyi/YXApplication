package com.example.songmachine.util;

import android.content.Context;
import android.widget.Toast;

public class MethodUtil {

    /**
     * toast分装类,减少每次写的麻烦
     * @param context context
     * @param content 吐司的内容
     */
    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

}
