package com.example.songmachine.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 文件过滤的类，判断当前文件属于哪类文件
 * Created by Administrator on 2018/2/6.
 */

public class FileFilter {

    public static final String[] VIDEO_EXTENSIONS = {"test", "3gp", "wmv", "ts", "3gp2", "rmvb", "mp4", "mov", "m4v", "avi", "3gpp", "3gpp2", "mkv", "flv", "divx", "f4v", "rm", "avb", "asf", "ram", "avs", "mpg", "v8", "swf", "m2v", "asx", "ra", "ndivx", "box", "xvid"};
    private static final HashSet<String> mHashVideo;

    static {
        mHashVideo = new HashSet<>(Arrays.asList(VIDEO_EXTENSIONS));
    }

    /**
     * 获取文件后缀
     */
    private static String getFileExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
     * 根据获得的文件的后缀名进行比较，判断文件是否属于video
     *
     * @param f 传进来的文件属性
     * @return 返回true或者false
     */
    public static boolean isVideo(File f) {
        final String ext = getFileExtension(f);
        return mHashVideo.contains(ext);
    }
}
