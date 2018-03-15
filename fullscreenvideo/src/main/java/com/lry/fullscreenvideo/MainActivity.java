package com.lry.fullscreenvideo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowId;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public SurfaceView mSurfaceView;
    public SurfaceHolder mHolder;
    public MediaPlayer mMediaPlayer;
    public Dialog mDialog;

    public Context context = MainActivity.this;
    public RelativeLayout surfaceLayout;
    private static final String KEY_PLAY_MODE = "play_mode"; // 播放的模式，小窗口or全屏
    private static final String KEY_PLAY_MODE_SMALL_VALUES = "small";
    private static final String KEY_PLAY_MODE_FULL_VALUES = "full";

    public static final String TAG = "liu";
    private int gravityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setPrefValues(context, KEY_PLAY_MODE, KEY_PLAY_MODE_SMALL_VALUES);
        surfaceLayout = this.findViewById(R.id.layout_surface);
        mSurfaceView = this.findViewById(R.id.surface_view);
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Settings.canDrawOverlays(context)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1111);
                } else {
                    resetSize();
                }
            }
        });
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mMediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(Utils.uri);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    // 重置layout尺寸，全屏，小窗口切换
    private void resetSize() {
        String values = Utils.getPrefValues(context, KEY_PLAY_MODE, null);
        if (values != null && values.equals(KEY_PLAY_MODE_SMALL_VALUES)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceLayout.setLayoutParams(params); // 全屏模式
            Utils.setPrefValues(context, KEY_PLAY_MODE, KEY_PLAY_MODE_FULL_VALUES);  // 全屏模式的时候设置的值
        } else if (values != null && values.equals(KEY_PLAY_MODE_FULL_VALUES)) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(600, 400);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            surfaceLayout.setLayoutParams(params); // 小窗口模式
            Utils.setPrefValues(context, KEY_PLAY_MODE, KEY_PLAY_MODE_SMALL_VALUES);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        gravityMode = surfaceLayout.getGravity();
        int width = surfaceLayout.getWidth();
        int height = surfaceLayout.getHeight();
        Log.e(TAG, "gravityMode: " + gravityMode + "  width: " + width + "  height: " + height);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}

