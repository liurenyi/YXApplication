package com.example.songmachine.util;

import android.os.Environment;

public class StorageCManager {

    public static final String TAG = "StorageCManager";

    public static final String filePath = ""; //测试用的uri路径

    /**
     * 获取内部sdcard的根目录
     *
     * @return 返回根目录
     */
    public static String getInnerSDcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

}
