package com.example.videoencryption.rxjava;

import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 文件类型判断
 * Created by Administrator on 2018/2/2.
 */

public class FileJudgment {

    private static final String[] VIDEO_EXTENSIONS = {"3gp", "wmv", "ts", "3gp2", "rmvb", "mp4", "mov", "m4v", "avi", "3gpp", "3gpp2", "mkv", "flv", "f4v", "rm", "avb", "asf", "ram", "avs", "mpg", "v8", "swf", "m2v", "asx", "ra", "box", "xvid"};

    private static final HashSet<String> mHashVideo;

    static {
        mHashVideo = new HashSet<>(Arrays.asList(VIDEO_EXTENSIONS));
    }

    /**
     * 获取文件后缀
     *
     * @return 返回后缀名
     */
    private static String getFileExtension(File file) {
        Log.e("liu","file" + file);
        if (file != null) {
            String name = file.getName();
            int i = name.lastIndexOf(".");
            if (i > 0 && i < name.length() - 1) {
                return name.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    public static boolean isVideoFile(File f) {
        String fileExtension = getFileExtension(f);
        return mHashVideo.contains(fileExtension);
    }

}
