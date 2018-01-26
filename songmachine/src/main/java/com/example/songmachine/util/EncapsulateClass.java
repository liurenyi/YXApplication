package com.example.songmachine.util;

import android.content.Context;
import android.media.AudioManager;
import android.view.WindowManager;

import com.example.songmachine.log.Logw;

/**
 * Created by Administrator on 2018/1/15.
 */

public class EncapsulateClass {

    public static final String TAG = "liu-EncapsulateClass";
    public static final int A_HOUR = 3600 * 1000;
    public static final int A_MINUTE = 60 * 1000;
    public static final int A_SECOND = 1000;

    public static WindowManager windowManager;
    public static AudioManager audioManager;

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        int hour;
        int minute;
        int second;
        if (time >= A_HOUR) {
            hour = (int) (time / (A_HOUR));
            minute = (int) ((time - (hour * A_HOUR)) / A_MINUTE);
            second = (int) ((time - hour * A_HOUR - minute * A_MINUTE) / A_SECOND);
            return hour + ":" + minute + ":" + second;
        } else if (time < A_HOUR && time >= A_MINUTE) {
            minute = (int) (time / A_MINUTE);
            second = (int) ((time - minute * A_MINUTE) / A_SECOND);
            return minute + ":" + second;
        } else if (time >= 0 && time < 10 * 1000) {
            second = (int) (time / A_SECOND);
            return "00:0" + second;
        } else {
            second = (int) (time / A_SECOND);
            return "00:" + second;
        }
    }

    public static int getHeight(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = windowManager.getDefaultDisplay().getHeight();
        Logw.i(TAG, "height: " + height);
        return height;
    }

    public static int getWidth(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        Logw.i(TAG, "width: " + width);
        return width;
    }

    public static void addVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }

    public static void reduceVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }

}
