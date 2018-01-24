package com.example.songmachine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.songmachine.util.StorageCManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private List<String> mPermissions = new ArrayList<>(); // mPermissions只包含必须要的权限,其他权限可去动态申请
    private static final int KEY_REQUEST_PERMISSION_CODE = 1;
    public VideoView mVideoVie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        setupVideo();
        // 当系统为6.0及以上，检查App权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkAppPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoVie.isPlaying()) {
            mVideoVie.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoVie.canPause()) {
            mVideoVie.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoVie.stopPlayback();
    }

    private void initUI() {
        mVideoVie = (VideoView) this.findViewById(R.id.video_main);
        Log.e("liu","mVideoVie: " + mVideoVie);
        mVideoVie.setMediaController(new MediaController(this));
        Uri uri = Uri.parse(StorageCManager.filePath);
        mVideoVie.setVideoURI(uri);
        mVideoVie.setOnPreparedListener(this);
        mVideoVie.setOnCompletionListener(this);
        mVideoVie.setOnErrorListener(this);
    }

    /**
     * 设置本地视频路径的uri,暂时紧作为测试专用
     */
    private void setupVideo() {

    }


    /**
     * 检查App是否拥有权限
     */
    private void checkAppPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        // 没有权限则去动态请求权限
        if (!mPermissions.isEmpty()) {
            String[] permission = mPermissions.toArray(new String[mPermissions.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permission, KEY_REQUEST_PERMISSION_CODE);
        }
    }

    /**
     * 请求权限返回结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case KEY_REQUEST_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, getResources().getString(
                                    R.string.app_request_permission_hint), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 视频准备的监听
     *
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mVideoVie.start();
    }

    /**
     * 视频播放完成的监听事件
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mVideoVie.stopPlayback();
    }

    /**
     * 视频播放错误的监听事件
     *
     * @param mediaPlayer
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mVideoVie.stopPlayback();
        return false;
    }
}
