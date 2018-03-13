package com.example.songmachine.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.WindowManager;

import com.example.songmachine.log.Logw;

import java.io.File;

import rx.Observable;
import rx.functions.Func1;

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
            return (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
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

    // 获取最大的音量值
    public static int getVolumeMax(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    // 获取当前音量值
    public static int getCurrentVolume(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    // 调节音量 (增加，减少)
    public static void adjustVolume(Context context, int values) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, values, AudioManager.FLAG_PLAY_SOUND);
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

    /**
     * 获取视频文件缩略图 API>=8(2.2)
     *
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     * MINI_KIND、MICRO_KIND、FULL_SCREEN_KIND
     */
    public static Bitmap getVideoThumb(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
    }

    // 获取当前路径文件的名字
    public static String getFileName(String path) {
        File file = new File(path);
        return formatSongName(file.getName());
    }

    // 格式化歌曲的名字，去掉后面的后缀
    private static String formatSongName(String name) {
        return name.substring(0, name.indexOf("."));
    }
}
