package com.example.songmachine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.songmachine.util.StorageCManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, View.OnClickListener {

    private List<String> mPermissions = new ArrayList<>(); // mPermissions只包含必须要的权限,其他权限可去动态申请
    private static final int KEY_REQUEST_PERMISSION_CODE = 1;
    private static final int KEY_MAIN_VIDEO_PLAYING = 2;
    private static final int KEY_MAIN_VIDEO_PAUSE = 3;
    private static final int KEY_MAIN_VIDEO_REPLAY = 4; // 重唱指令

    public VideoView mVideoVie;
    public RadioButton mRadioButton1;
    public RadioButton mRadioButton2;
    public RadioButton mRadioButton3;
    public RadioButton mRadioButton4;
    public RadioButton mRadioButton5;
    public RadioButton mRadioButton6;
    public RadioButton mRadioButton7;
    public RadioButton mRadioButton8;
    public RadioButton mRadioButton9;
    public RadioButton mRadioButton10;

    private Bitmap bitmap;
    private Message message;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEY_MAIN_VIDEO_PLAYING:
                    goPlaying();
                    break;
                case KEY_MAIN_VIDEO_PAUSE:
                    goPause();
                    break;
                case KEY_MAIN_VIDEO_REPLAY:
                    goReplay();
                    break;
            }
        }
    };

    /**
     * 重新开始唱歌
     */
    private void goReplay() {
        mVideoVie.seekTo(0);
    }

    /**
     * 开始播放视频
     */
    private void goPlaying() {
        if (!mVideoVie.isPlaying() && mVideoVie != null) {
            mVideoVie.start();
            mVideoVie.setBackground(null);
        }
    }

    /**
     * 暂停视频
     */
    private void goPause() {
        if (mVideoVie != null) {
            mVideoVie.pause();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
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
        // mVideoVie.setMediaController(new MediaController(this));
        Uri uri = Uri.parse(StorageCManager.filePath); // 测试路径
        createVideoThumbnail(StorageCManager.filePath);
        mVideoVie.setVideoURI(uri);
        mVideoVie.setOnPreparedListener(this);
        mVideoVie.setOnCompletionListener(this);
        mVideoVie.setOnErrorListener(this);

        mRadioButton1 = (RadioButton) this.findViewById(R.id.radio_left_1);
        mRadioButton2 = (RadioButton) this.findViewById(R.id.radio_left_2);
        mRadioButton3 = (RadioButton) this.findViewById(R.id.radio_left_3);
        mRadioButton4 = (RadioButton) this.findViewById(R.id.radio_left_4);
        mRadioButton5 = (RadioButton) this.findViewById(R.id.radio_left_5);
        mRadioButton6 = (RadioButton) this.findViewById(R.id.radio_right_1);
        mRadioButton7 = (RadioButton) this.findViewById(R.id.radio_right_2);
        mRadioButton8 = (RadioButton) this.findViewById(R.id.radio_right_3);
        mRadioButton9 = (RadioButton) this.findViewById(R.id.radio_right_4);
        mRadioButton10 = (RadioButton) this.findViewById(R.id.radio_right_5);

        mRadioButton1.setOnClickListener(this);
        mRadioButton2.setOnClickListener(this);
        mRadioButton3.setOnClickListener(this);
        mRadioButton4.setOnClickListener(this);
        mRadioButton5.setOnClickListener(this);
        mRadioButton6.setOnClickListener(this);
        mRadioButton7.setOnClickListener(this);
        mRadioButton8.setOnClickListener(this);
        mRadioButton9.setOnClickListener(this);
        mRadioButton10.setOnClickListener(this);

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
        //mVideoVie.setBackground(new BitmapDrawable(bitmap)); // 获取要播放的视频的第一帧
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_left_1:
                break;
            case R.id.radio_left_2:
                message = new Message();
                message.what = KEY_MAIN_VIDEO_REPLAY;
                handler.sendMessage(message);
                break;
            case R.id.radio_left_3:
                break;
            case R.id.radio_left_4:
                message = new Message();
                message.what = mVideoVie.isPlaying() ? KEY_MAIN_VIDEO_PAUSE : KEY_MAIN_VIDEO_PLAYING;
                //setCompoundDrawables图片资源替换不成功
                /*mRadioButton4.setCompoundDrawables(null,
                        mVideoVie.isPlaying() ? getResources().getDrawable(R.drawable.ic_pause_circle_outline) :
                                getResources().getDrawable(R.drawable.ic_play_arrow), null, null);*/
                //mRadioButton4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_play_arrow), null, null);
                mRadioButton4.setCompoundDrawablesWithIntrinsicBounds(null,
                        mVideoVie.isPlaying() ? getResources().getDrawable(R.drawable.ic_play_arrow) :
                                getResources().getDrawable(R.drawable.ic_pause_circle_outline), null, null);
                mRadioButton4.setText(mVideoVie.isPlaying() ? getResources().getString(R.string.radio_button_left_4_text_1) :
                        getResources().getString(R.string.radio_button_left_4_text));
                handler.sendMessage(message);
                break;
            case R.id.radio_left_5:
                break;
            case R.id.radio_right_1:
                break;
            case R.id.radio_right_2:
                break;
            case R.id.radio_right_3:
                break;
            case R.id.radio_right_4:
                break;
            case R.id.radio_right_5:
                break;
        }
    }

    /**
     * 获取视频第一帧
     *
     * @return
     */
    private void createVideoThumbnail(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        File file = new File(filePath);
        if (file.exists()) {
            retriever.setDataSource(file.getAbsolutePath());
            bitmap = retriever.getFrameAtTime();
            if (bitmap == null) {
                Toast.makeText(MainActivity.this, "获取视频第一帧失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "视频文件不存在", Toast.LENGTH_LONG).show();
        }
    }

}
