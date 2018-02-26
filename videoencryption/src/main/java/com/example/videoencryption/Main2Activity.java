package com.example.videoencryption;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.videoencryption.util.MethodUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private static final String KEY_YUXINKEJI_ENCRYP = "~"; //加密标记  126
    private static final String KEY_YUXINKEJI_DECRYP = "."; // 解密标记  46
    private int encrypLen = 100; //加解密文件的长度均为100字节
    private List<String> mPermissions = new ArrayList<>(); // mPermissions只包含必须要的权限,其他权限可去动态申请
    private byte[] bytesEncryp;
    private byte[] bytesDecryp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
        try {
            bytesEncryp = KEY_YUXINKEJI_ENCRYP.getBytes("UTF-8");
            bytesDecryp = KEY_YUXINKEJI_DECRYP.getBytes("UTF-8");
            String string = Arrays.toString(bytesEncryp);
            String string1 = Arrays.toString(bytesDecryp);
            Log.e("liu", "string: " + string + "string1: " + string1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //String path = CStorageManager.getInnerSDCardPath() + "/test.mp4";
        String path = "mnt/sdcard/test.mkv";
        /*isEncrypt(path);*/
        if (isEncryption(path)) {
            MethodUtil.log("liu", "文件已加密，执行解密操作", 5);
            encrypFile(path, false/*是否添加加密的标志符*/);
        } else {
            MethodUtil.log("liu", "文件未加密，执行加密操作", 5);
            encrypFile(path, true/*是否添加加密的标志符*/);
        }
    }

    /**
     * 检查App是否拥有权限
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        // 没有权限则去动态请求权限
        if (!mPermissions.isEmpty()) {
            String[] permission = mPermissions.toArray(new String[mPermissions.size()]);
            ActivityCompat.requestPermissions(Main2Activity.this, permission, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            MethodUtil.toast(Main2Activity.this, "必须同意此权限才能正常使用App");
                            finish();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 加解密文件内容
     *
     * @param filePath 文件的路径
     */
    public void encrypFile(String filePath, boolean isEncryption) {
        File file = new File(filePath);
        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
            MappedByteBuffer buffer = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, accessFile.length());
            // 对文件前100字节做异或运算处理
            byte tmp;
            for (int i = 0; i < encrypLen; i++) {
                byte rawByte = buffer.get(i);
                tmp = (byte) (rawByte ^ i); // 做异或运算
                buffer.put(i, tmp);
            }
            // 如果要对文件加密，则文件最后一个字节改为加密的标志，否则，反之。
            if (isEncryption) {
                buffer.put((int) (accessFile.length() - 1), bytesEncryp[0]);
                MethodUtil.log("liu", "加密成功", 5);
            } else {
                buffer.put((int) (accessFile.length() - 1), bytesDecryp[0]);
                MethodUtil.log("liu", "解密成功", 5);
            }
            buffer.force();
            buffer.clear();
            accessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件是否加密
     */
    public boolean isEncryption(String string) {
        File file = new File(string);
        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
            long length = accessFile.length(); //文件长度，多少字节
            MethodUtil.log("liu", "length: " + length, 5);
            MappedByteBuffer buffer = accessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
            byte b = buffer.get((int) length - 1); // 读取最后一个字节标志
            MethodUtil.log("liu", "最后一个字节标志位：" + b, 5);
            buffer.force();
            buffer.clear();
            accessFile.close();
            if (bytesEncryp[0] == b) { // 如果文件最后一个字符是加密的标志符，返回值为true
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MethodUtil.log("liu", "有错误，请排查！", 5);
        }
        return false;
    }
}
