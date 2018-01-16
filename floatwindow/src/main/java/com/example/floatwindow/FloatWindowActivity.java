package com.example.floatwindow;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.floatwindow.log.logw;

public class FloatWindowActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener {

    public Button btnFloatWindow;
    public Button btnPlayVideo;
    public VideoView videoView;
    public String filePath = "/mnt/sdcard/test.mp4";
    public static final int STOP_SERVICE = 1;
    public static final int START_SERVICE = 2;
    public Message message;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP_SERVICE:
                    Intent intent = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                    stopService(intent);
                    break;
                case START_SERVICE:
                    Intent intent1 = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                    startService(intent1);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_window);

        btnFloatWindow = (Button) this.findViewById(R.id.create_float_window);
        btnPlayVideo = (Button) this.findViewById(R.id.play);
        btnFloatWindow.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);
        videoView = (VideoView) this.findViewById(R.id.video_view);
        Uri uri = Uri.parse(filePath);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.setOnCompletionListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_float_window:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    } else {
                        Intent intent = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                        startService(intent);
                    }
                }
                break;
            case R.id.play:
                if (!videoView.isPlaying()){
                    videoView.start();
                    videoView.requestFocus();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MENU) {
            message = new Message();
            message.what = STOP_SERVICE;
            handler.sendMessage(message);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        logw.i("liu","...");
    }
}
