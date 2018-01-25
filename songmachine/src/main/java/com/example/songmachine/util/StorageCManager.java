package com.example.songmachine.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.widget.Toast;

import com.example.songmachine.MainActivity;
import com.example.songmachine.log.Logw;

import java.io.File;

/**
 * Created by Administrator on 2018/1/24.
 */

public class StorageCManager {

    public static final String TAG = "StorageCManager";

    @SuppressLint("SdCardPath")
    public static final String filePath = "/mnt/sdcard/1234.mkv"; //临时测试的视频路径

    @SuppressLint("SdCardPath")
    public static final String filePath1 = "/mnt/sdcard/test.mp4";


    /**
     * 获取视频第一帧
     *
     * @return
     */
    public static Bitmap createVideoThumbnail(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        File file = new File(filePath);
        Bitmap bitmap = null;
        if (file.exists()) {
            retriever.setDataSource(file.getAbsolutePath());
            bitmap = retriever.getFrameAtTime();
            if (bitmap == null) {
                Logw.e(TAG, "获取第一帧失败");
            }
        } else {
            Logw.e(TAG, "视频文件不存在");
        }
        return bitmap;
    }

}
