package com.example.floatwindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floatwindow.log.logw;
import com.example.floatwindow.util.EncapsulateClass;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service implements View.OnClickListener,
        SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener,
        SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "liu-FloatWindowService";
    public Button btnTest;
    public ImageView imgFastForward; //快进
    public ImageView imgFastRewind; // 后退
    public ImageView imgPlayAndPause; // 暂停
    public ImageView imgKeyboardReturn; // 退出
    public SeekBar seekBarVideoProgress; // 视频进度条
    public TextView textVideoTime;  // 视频当前时间进度
    public TextView textToggleVideoTime; //视频总共时长
    public boolean isPlaying = false; //是否正在播放
    public RelativeLayout videoControl;
    public TimerTask timerTask;
    public Timer timer;
    public View view;
    public MediaPlayer mediaPlayer;
    public SurfaceView surfaceView;
    public WindowManager windowManager;
    public WindowManager.LayoutParams layoutParams;
    public int FloatWindowWidth;
    public int FloatWindowHeight;

    public static final int SIGNAL_FAST_FORWARD = 1;
    public static final int SIGNAL_FAST_REWIND = 2;
    public static final int SIGNAL_PLAY_AND_PAUSE = 3;
    public static final int SIGNAL_KEYBOARD_RETURN = 4;
    public static final int SIGNAL_UPDATE_PLAYING_VIDEO_TIME = 5;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SIGNAL_FAST_FORWARD:
                    break;
                case SIGNAL_FAST_REWIND:
                    break;
                case SIGNAL_PLAY_AND_PAUSE:
                    if (isPlaying) {
                        mediaPlayer.pause();
                        isPlaying = false;
                        updateImgForPlaying(isPlaying);
                    } else {
                        mediaPlayer.start();
                        isPlaying = true;
                        updateImgForPlaying(isPlaying);
                    }
                    break;
                case SIGNAL_KEYBOARD_RETURN:
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    windowManager.removeView(view);
                    stopSelf();
                    break;
                case SIGNAL_UPDATE_PLAYING_VIDEO_TIME:
                    textVideoTime.setText(EncapsulateClass.formatTime(mediaPlayer.getCurrentPosition())); //设置当前位置的时间的进度
                    break;
                default:
                    break;
            }
        }
    };

    public FloatWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logw.e(TAG, "-->onCreate()");
        createFloatWindow();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        logw.e(TAG, "-->onStart()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logw.e(TAG, "-->onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerTask.cancel(); // 退出悬浮窗口时候，取消计时器任务。
//        windowManager.removeView(view);
        Message message = new Message();
        message.what = SIGNAL_KEYBOARD_RETURN;
        handler.sendMessage(message);
        logw.e(TAG, "-->onDestroy()");
    }

    private void createFloatWindow() {
        // 测试btn
        btnTest = new Button(getApplicationContext());
        btnTest.setText("警告");
        btnTest.setBackgroundResource(R.mipmap.ic_launcher);

        View view = initFloatUI();

        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        // 设置window type
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT; // TYPE_SYSTEM_ALERT,创建出来的窗口可拖动
        //layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY; // TYPE_SYSTEM_OVERLAY,创建出来的窗口不可拖动
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */
        // 设置图片格式，效果为背景透明
        //layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.format = PixelFormat.TRANSPARENT;
        // 设置Window flag
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        /*
         * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
        FloatWindowHeight = EncapsulateClass.getHeight(getApplicationContext()) * 3 / 4;
        FloatWindowWidth = EncapsulateClass.getWidth(getApplicationContext()) * 3 / 4;
        logw.i(TAG, "FloatWindowWidth: " + FloatWindowWidth + " FloatWindowHeight: " + FloatWindowHeight);
        layoutParams.width = FloatWindowWidth;
        layoutParams.height = FloatWindowHeight;
        // 监听悬浮窗口事件
        view.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        logw.i(TAG, "-->MotionEvent.ACTION_DOWN");
                        lastX = (int) motionEvent.getRawX();
                        lastY = (int) motionEvent.getRawY();
                        paramX = layoutParams.x;
                        paramY = layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        logw.i(TAG, "-->MotionEvent.ACTION_MOVE");
                        int dx = (int) motionEvent.getRawX() - lastX;
                        int dy = (int) motionEvent.getRawY() - lastY;
                        layoutParams.x = paramX + dx;
                        layoutParams.y = paramY + dy;
                        windowManager.updateViewLayout(view, layoutParams); //实现可拖动功能
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        windowManager.addView(view, layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fast_forward:
                Toast.makeText(getApplicationContext(), "快进", Toast.LENGTH_LONG).show();
                break;
            case R.id.fast_rewind:
                Toast.makeText(getApplicationContext(), "快退", Toast.LENGTH_LONG).show();
                break;
            case R.id.play_and_pause:
                Message message2 = new Message();
                message2.what = SIGNAL_PLAY_AND_PAUSE;
                handler.sendMessage(message2);
                break;
            case R.id.keyboard_return:
                stopSelf(); // 停止服务，使其走OnDestroy方法，然后移除悬浮窗。
                break;
            default:
                break;
        }
    }

    /**
     * 初始化悬浮窗的UI界面
     */
    private View initFloatUI() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        view = inflater.inflate(R.layout.video_display, null, false);
        imgFastForward = view.findViewById(R.id.fast_forward);
        imgFastRewind = view.findViewById(R.id.fast_rewind);
        imgPlayAndPause = view.findViewById(R.id.play_and_pause);
        imgKeyboardReturn = view.findViewById(R.id.keyboard_return);
        seekBarVideoProgress = view.findViewById(R.id.video_progress);
        textVideoTime = view.findViewById(R.id.video_time);
        textToggleVideoTime = view.findViewById(R.id.video_toggle_time);
        videoControl = view.findViewById(R.id.video_control);
        imgFastForward.setOnClickListener(this);
        imgFastRewind.setOnClickListener(this);
        imgPlayAndPause.setOnClickListener(this);
        imgKeyboardReturn.setOnClickListener(this);
        seekBarVideoProgress.setOnSeekBarChangeListener(this);

        surfaceView = view.findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this); //回调，检测surfaceview的三种状态
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //surfaceview的显示源类型

        // 初始化播放视频组件
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);

        String filePath = "/mnt/sdcard/test.mp4";
        try {
            mediaPlayer.setDataSource(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        logw.i(TAG, "-->surfaceCreated()");
        mediaPlayer.setDisplay(surfaceHolder);
        try {
            mediaPlayer.prepare();
            seekBarVideoProgress.setMax(mediaPlayer.getDuration());
            textToggleVideoTime.setText(EncapsulateClass.formatTime(mediaPlayer.getDuration())); //获取视频总共时间
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        seekBarVideoProgress.setProgress(mediaPlayer.getCurrentPosition());
                        Message message = new Message();
                        message.what = SIGNAL_UPDATE_PLAYING_VIDEO_TIME;
                        message.arg1 = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(message);
                    }
                }
            };
            timer.schedule(timerTask, 0, 1000);
            mediaPlayer.start();
            isPlaying = true;
            updateImgForPlaying(isPlaying);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        logw.i(TAG, "-->surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        logw.i(TAG, "-->surfaceDestroyed()");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //windowManager.removeView(view);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        logw.i(TAG, "-->onCompletion()");
        isPlaying = false;
        updateImgForPlaying(isPlaying);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        logw.i(TAG, "-->onError()");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        logw.i(TAG, "-->onPrepared()");
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        logw.i(TAG, "-->onSeekComplete()");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
        logw.i(TAG, "-->onVideoSizeChanged()");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        logw.i(TAG, "-->onProgressChanged()");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        logw.i(TAG, "-->onStartTrackingTouch()");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        logw.i(TAG, "-->onStopTrackingTouch()");
        mediaPlayer.seekTo(seekBar.getProgress());
    }

    private void updateImgForPlaying(Boolean b) {
        int resid = b ? R.drawable.ic_pause_circle_outline_24dp : R.drawable.ic_play_arrow_24dp;
        imgPlayAndPause.setBackgroundResource(resid);
    }
}
