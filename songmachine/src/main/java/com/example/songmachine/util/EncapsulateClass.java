package com.example.songmachine.util;

import android.content.Context;
import android.media.AudioManager;
import android.view.WindowManager;

import com.example.songmachine.log.Logw;

import java.io.File;

import rx.Observable;
import rx.functions.Func1;

/**
 * 封装类
 * Created by Administrator on 2018/1/15.
 */

public class EncapsulateClass {

    private static final String TAG = "EncapsulateClass";
    private static final int A_HOUR = 3600 * 1000;
    private static final int A_MINUTE = 60 * 1000;
    private static final int A_SECOND = 1000;

    private static WindowManager windowManager;
    private static AudioManager audioManager;

    /**
     * 格式化时间
     *
     * @param time 时间
     * @return 返回值
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

    /**
     * 增加音乐音量，此处只需要处理音乐的音量就可。
     *
     * @param context 对象
     */
    public static void addVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }

    /**
     * 减少音乐音量，此处只需要处理音乐的音量就可。
     *
     * @param context 对象
     */
    public static void reduceVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }

    /**
     * rxjava递归查询内存中的视频文件
     *
     * @param file 传进来的file
     * @return 返回值
     */
    public static Observable<File> listFiles(final File file) {
        if (file.isDirectory()) {
            return Observable.from(file.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(file).filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return file.canRead() && file.exists() && FileFilter.isVideo(file);
                }
            });
        }
    }

}
