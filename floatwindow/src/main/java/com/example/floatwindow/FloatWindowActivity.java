package com.example.floatwindow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.floatwindow.log.logw;
import com.example.floatwindow.util.EncapsulateClass;

import java.io.File;

/**
 * videoView使用方式
 * https://www.jianshu.com/p/2d3b221a2ee7
 */
public class FloatWindowActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    public Button btnFloatWindow;
    public Button btnPlayVideo;
    public VideoView videoView;
    public String filePath = "/mnt/sdcard/test.mp4";
    public static final int STOP_SERVICE = 1;
    public static final int START_SERVICE = 2;
    public Message message;
    public Bitmap bitmap;
    public GestureDetector gestureDetector;


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
        videoView.setOnPreparedListener(this);
        createVideoThumbnail();

        // 监听视频播放的区域，把监听到的ontouch事件交给gesture去处理
        gestureDetector = new GestureDetector(this, gestureListener);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
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
                if (!videoView.isPlaying()) {
                    videoView.start();
                    videoView.requestFocus();
                    videoView.setBackground(null);
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
        logw.i("liu", "...");
    }

    /**
     * 获取视频第一帧
     *
     * @return
     */
    private void createVideoThumbnail() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        File file = new File(filePath);
        if (file.exists()) {
            retriever.setDataSource(file.getAbsolutePath());
            bitmap = retriever.getFrameAtTime();
            if (bitmap == null) {
                Toast.makeText(FloatWindowActivity.this, "获取视频第一帧失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(FloatWindowActivity.this, "视频文件不存在", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoView.setBackground(new BitmapDrawable(bitmap)); //视频准备好之后设置画面设置为第一帧画面
    }

    /**
     * 手势监听事件
     */
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {
        /**
         * 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            logw.i("liu", "-->onDown()");
            return true;
        }

        /**
         * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
         * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
         * @param motionEvent
         */
        @Override
        public void onShowPress(MotionEvent motionEvent) {
            logw.i("liu", "-->onShowPress()");
        }

        /**
         *  用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            logw.i("liu", "-->onSingleTapUp()");
            return false;
        }

        /**
         *用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
         * @param motionEvent
         * @param motionEvent1
         * @param v
         * @param v1
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            final int SCROLL_MIN_DISTANCE = 10;
            final int SCROLL_MIN_VELOCITY = 10;
            if (motionEvent.getX() - motionEvent1.getX() >= SCROLL_MIN_DISTANCE && Math.abs(v) > SCROLL_MIN_VELOCITY) {
                logw.i("liu", "scroll left");
                EncapsulateClass.reduceVolume(FloatWindowActivity.this);
            } else if (motionEvent.getX() - motionEvent1.getX() < SCROLL_MIN_DISTANCE && Math.abs(v) > SCROLL_MIN_VELOCITY) {
                EncapsulateClass.addVolume(FloatWindowActivity.this);
            }
            return true;
        }

        /**
         * 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
         * @param motionEvent
         */
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            logw.i("liu", "-->onLongPress()");
        }

        /**
         * 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
         * @param motionEvent
         * @param motionEvent1
         * @param v
         * @param v1
         * @return
         */
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            logw.i("liu", "-->onFling()");
            return false;
        }
    };
}
