package com.example.videoencryption;

import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class Main2Activity extends AppCompatActivity {

    private static final String KEY_YUXINKEJI_ENCRYP = "~"; //加密标记
    private static final String KEY_YUXINKEJI_DECRYP = "DEL"; // 解密标记
    private byte[] bytesEncryp;
    private byte[] bytesDecryp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            bytesEncryp = KEY_YUXINKEJI_ENCRYP.getBytes("UTF-8");
            bytesDecryp = KEY_YUXINKEJI_DECRYP.getBytes("UTF-8");
            String string = Arrays.toString(bytesEncryp);
            String string1 = Arrays.toString(bytesDecryp);
            Log.e("liu", "string: " + string);
            Log.e("liu", "string1: " + string1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String path = CStorageManager.getInnerSDCardPath() + "/test.3gp";
        /*isEncrypt(path);*/
        isEncryption(path);
    }

    public boolean isEncrypt(String string) {
        File file = new File(string);
        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
            long length = accessFile.length(); //文件长度，多少字节
            MappedByteBuffer buffer = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
            for (int i = (int) (length - 1); i < length; i++) {
                byte b = buffer.get(i);
                buffer.put(i, bytesEncryp[0]); // 映射加密之后的标记
                Log.e("liu", "b: " + b);
            }
            buffer.force();
            buffer.clear();
            accessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("liu", "有错误，请排查!");
        }
        return false;
    }

    public boolean isEncryption(String string) {
        File file = new File(string);
        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
            long length = accessFile.length(); //文件长度，多少字节
            MappedByteBuffer buffer = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
            byte b = buffer.get((int) length - 1); // 读取最后一个字节标志
            //Log.e("liu", "最后一个字节标志位：" + b);
            buffer.force();
            buffer.clear();
            accessFile.close();
            boolean as = bytesEncryp[0] == b;
            Log.e("liu", "as : " + as);
            if (bytesEncryp[0] == b) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("liu", "有错误，请排查!");
        }
        return false;
    }
}
