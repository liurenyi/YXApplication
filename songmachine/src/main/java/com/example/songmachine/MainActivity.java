package com.example.songmachine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.songmachine.adapter.RecyclerAdapter;
import com.example.songmachine.adapter.SongNumberAdapter;
import com.example.songmachine.display.DifferentDisplay;
import com.example.songmachine.util.EncapsulateClass;
import com.example.songmachine.util.MethodUtil;
import com.example.songmachine.util.StorageCManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private List<String> mPermissions = new ArrayList<>(); // mPermissions只包含必须要的权限,其他权限可去动态申请
    private static final int KEY_REQUEST_PERMISSION_CODE = 1;
    private static final int KEY_MAIN_VIDEO_PLAYING = 2;
    private static final int KEY_MAIN_VIDEO_PAUSE = 3;
    private static final int KEY_MAIN_VIDEO_REPLAY = 4; // 重唱指令
    private static final int KEY_UPDATE_ITEM = 5;
    private static final int KEY_START_FLOATWINDOWS = 6;
    private static final int KEY_SHOW_VOLUME_UI = 7; // 调节音量指令

    public SurfaceView surfaceViewMain;
    public SurfaceHolder holder;
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
    public RecyclerView mRecyclerView;
    private MediaPlayer mMediaPlayer;
    private TextView selectedNumber;
    public Dialog dialog;
    public View inflate;
    public ListView lvSongNumber;
    public SongNumberAdapter snAdapter;
    public ImageView imgVolumeDown, imgVolumeUp;
    public SeekBar seekBarVolume;

    private RecyclerAdapter adapter;
    private Message message;
    private Intent intent;
    public DisplayManager mDisplayManager;
    public Display[] mDisplays;
    private DifferentDisplay mDifferentDislay;
    private boolean isYuanChang = true;

    private List<Map<String, Object>> mapList = new ArrayList<>();

    private List<Map<String, String>> selectedMapList = new ArrayList<>(); // 已点的歌曲数目的集合

    @SuppressLint("HandlerLeak")
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
                case KEY_UPDATE_ITEM:
                    adapter.notifyDataSetChanged();
                    break;
                case KEY_START_FLOATWINDOWS:
                    // 开启悬浮窗之前先请求权限
                    askForPermission();
                    break;
                case KEY_SHOW_VOLUME_UI:
                    adjustVolumeLayout();
                    break;

            }
        }
    };

    private void startFService() {
        intent = new Intent(MainActivity.this, FloatWindowService.class);
        // startService(intent); 暂时注释便于调试。
    }

    private void askForPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(MainActivity.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
            intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1111);
        } else {
            startFService(); // 开启悬浮窗的服务
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1111) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                startFService();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRxJava();
        // 当系统为6.0及以上，检查App权限
        if (Build.VERSION.SDK_INT >= 23) {
            checkAppPermission();
            askForPermission();
        }
        // 实现副屏的功能代码逻辑
        mDisplayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
        mDisplays = mDisplayManager.getDisplays();
        mDifferentDislay = new DifferentDisplay(this, mDisplays[mDisplays.length - 1]); // displays[1]是副屏 (现在目前只有一个屏幕,VGA+HDMI作为二个屏幕)
        mDifferentDislay.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        // mDifferentDislay.show();  显示副屏，功能已经成功，暂时注释用于调试。
        mMediaPlayer = new MediaPlayer(); // 初始化
        mMediaPlayer.setOnCompletionListener(this);
        selectedNumber = (TextView) this.findViewById(R.id.tv_selected_number);
    }

    /**
     * 实现RxJava遍历文件中所有视频文件（初始化）
     */
    private void initRxJava() {
        File filePath = new File(StorageCManager.getInnerSDcardPath());
        Observable.just(filePath).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return EncapsulateClass.listFiles(file);
            }
        }).doOnNext(new Action1<File>() {
            @Override
            public void call(File file) {
                String videoPath = file.getPath();
                Bitmap videoThumb = EncapsulateClass.getVideoThumb(videoPath);
                Map<String, Object> map1 = new HashMap<>();
                map1.put("image", videoThumb);
                mapList.add(map1);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<File>() {
            @Override
            public void onCompleted() { // 此方法只在初始化的时候执行一次，把本地所有歌曲，全部添加到已点列表。
                Log.e("liu", "已点歌曲:" + selectedMapList.size());
                selectedNumber.setText(selectedMapList.size() + "");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(File file) {
                String path = file.getPath();
                String videoName = file.getName();
                Map<String, String> map = new HashMap<>();
                map.put("videoName", videoName);
                map.put("videoPath", path);
                selectedMapList.add(map);
                if (adapter == null) {
                    adapter = new RecyclerAdapter(MainActivity.this, mapList);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new RecyclerAdapterListener.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            message = new Message();
                            message.what = KEY_START_FLOATWINDOWS;
                            handler.sendMessage(message);
                        }
                    });
                }
                adapter.notifyDataSetChanged(); // 通知adapter更新数据
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
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

    private void initUI() {
        Uri uri = Uri.parse("/storage/emulated/0/Movies/60072668.mkv"); // 测试路径
        surfaceViewMain = (SurfaceView) this.findViewById(R.id.surface_main);
        surfaceViewMain.setKeepScreenOn(true);
        holder = surfaceViewMain.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
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

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(uri.toString());
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        /*adapter = new RecyclerAdapter(MainActivity.this, mapList);
        mRecyclerView.setAdapter(adapter);*/
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 15, true));
    }

    // 检查App是否拥有权限
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

    // 请求权限返回结果
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
                message.what = mMediaPlayer.isPlaying() ? KEY_MAIN_VIDEO_PAUSE : KEY_MAIN_VIDEO_PLAYING;
                //setCompoundDrawables图片资源替换不成功
                /*mRadioButton4.setCompoundDrawables(null,
                        mVideoVie.isPlaying() ? getResources().getDrawable(R.drawable.ic_pause_circle_outline) :
                                getResources().getDrawable(R.drawable.ic_play_arrow), null, null);*/
                /*mRadioButton4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().
                getDrawable(R.drawable.ic_play_arrow), null, null);*/
                mRadioButton4.setCompoundDrawablesWithIntrinsicBounds(null,
                        mMediaPlayer.isPlaying() ? getResources().getDrawable(R.drawable.ic_play_arrow_black) :
                                getResources().getDrawable(R.drawable.ic_pause_circle_filled_black), null, null);
                mRadioButton4.setText(mMediaPlayer.isPlaying() ? getResources().getString(R.string.radio_button_left_4_text_1) :
                        getResources().getString(R.string.radio_button_left_4_text));
                handler.sendMessage(message);
                break;
            case R.id.radio_left_5:
                message = new Message();
                message.what = KEY_SHOW_VOLUME_UI;
                handler.sendMessage(message);
                break;
            case R.id.radio_right_1: // 切歌功能按键
                cutSongs();
                break;
            case R.id.radio_right_2: // 伴唱功能按键
                VA();
                break;
            case R.id.radio_right_3: // 已点功能按键
                showSongList();
                break;
            case R.id.radio_right_4: // 气氛功能按键
                break;
            case R.id.radio_right_5: // 返回功能按键
                break;
            case R.id.img_volume_down:
                int currentVolume = EncapsulateClass.getCurrentVolume(MainActivity.this);
                if (currentVolume > 0) {
                    EncapsulateClass.adjustVolume(MainActivity.this, currentVolume - 1);
                    seekBarVolume.setProgress(currentVolume - 1);
                } else if (currentVolume == 0) {
                    MethodUtil.toast(this, getString(R.string.ui_main_volume_text));
                } else {
                    MethodUtil.toast(this, getString(R.string.ui_main_volume_text_1));
                }
                break;
            case R.id.img_volume_up:
                int currentVolume1 = EncapsulateClass.getCurrentVolume(MainActivity.this);
                int volumeMax = EncapsulateClass.getVolumeMax(MainActivity.this);
                if (currentVolume1 < volumeMax) {
                    EncapsulateClass.adjustVolume(MainActivity.this, currentVolume1 + 1);
                    seekBarVolume.setProgress(currentVolume1 + 1);
                } else {
                    MethodUtil.toast(this, getString(R.string.ui_main_volume_text_2));
                }
                break;
        }
    }


    /**
     * 重新开始唱歌,主屏副屏同步
     */
    private void goReplay() {
        mMediaPlayer.seekTo(0); // 主屏播放界面重唱
        if (mDifferentDislay != null) {
            mDifferentDislay.reset(); // 副屏播放界面重唱
        }
    }

    /**
     * 开始播放视频,主屏副屏同步
     */
    private void goPlaying() {
        if (!mMediaPlayer.isPlaying() && mMediaPlayer != null) {
            mMediaPlayer.start();
            if (mDifferentDislay != null) {
                mDifferentDislay.play();
            }
        }
    }

    /**
     * 暂停视频,主屏副屏同步
     */
    private void goPause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
        if (mDifferentDislay != null) {
            mDifferentDislay.pause();
        }
    }

    /**
     * 切歌功能
     */
    private void cutSongs() {
        if (selectedMapList.size() > 0) { // 表示有已点的歌曲
            String path = selectedMapList.get(0).get("videoPath");
            mMediaPlayer.reset(); // 重置mMediaPlayer
            try {
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PTOS();
            selectedMapList.remove(0);
            selectedNumber.setText(selectedMapList.size() + "");
        } else {
            MethodUtil.toast(MainActivity.this, getString(R.string.tv_songs_alter_text));
            mMediaPlayer.setLooping(true); // 已点列表无歌曲，当前歌曲循环播放
        }
    }

    /**
     * VA --> vocal accompaniment 伴唱功能的方法
     */
    private void VA() {
        if (mMediaPlayer != null) {
            MediaPlayer.TrackInfo[] trackInfo = mMediaPlayer.getTrackInfo();
            if (trackInfo != null && trackInfo.length > 0) {
                Log.e("liu", "TrackInfo length: " + trackInfo.length);
                if (trackInfo.length >= 3) { // 如果大于等于3，则视频文件支持伴唱原唱功能
                    if (isYuanChang) {
                        mMediaPlayer.selectTrack(2); // 伴唱
                        isYuanChang = false;
                        mRadioButton7.setText("原唱");
                    } else {
                        mMediaPlayer.selectTrack(1); // 原唱
                        isYuanChang = true;
                        mRadioButton7.setText("伴唱");
                    }
                }
            }
        }
    }

    /**
     * PTOS --> Perform the original song
     * 强制执行原唱功能
     */
    private void PTOS() {
        MediaPlayer.TrackInfo[] trackInfo = mMediaPlayer.getTrackInfo();
        if (trackInfo != null && trackInfo.length > 0) {
            mMediaPlayer.selectTrack(1); // 切换到原唱功能
            isYuanChang = true;
            mRadioButton7.setText("伴唱");
        }
    }

    private void showSongList() { // 显示已点歌曲的详细情况
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.selected_song_number, null);
        lvSongNumber = inflate.findViewById(R.id.lv_song_number);
        snAdapter = new SongNumberAdapter(MainActivity.this, selectedMapList);
        lvSongNumber.setAdapter(snAdapter);
        dialog.setContentView(inflate);
        Window window = dialog.getWindow();
        WindowManager manager = dialog.getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        //window.setGravity(Gravity.RIGHT);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = display.getWidth() / 2;   // 弹出框宽度
        layoutParams.height = display.getHeight() / 2;
        window.setAttributes(layoutParams);
        dialog.show();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) { // 播放完成的监听事件
        cutSongs();
    }

    // 自定义的音量调节布局
    private void adjustVolumeLayout() {
        dialog = new Dialog(MainActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.volume_adjust, null);
        imgVolumeDown = inflate.findViewById(R.id.img_volume_down);
        imgVolumeUp = inflate.findViewById(R.id.img_volume_up);
        imgVolumeDown.setOnClickListener(this);
        imgVolumeUp.setOnClickListener(this);
        seekBarVolume = inflate.findViewById(R.id.seek_bar_volume);
        // seek_bar监听事件
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0 && i < 75) { // STREAM_MUSIC的最大值为75，此处直接写75，减去频繁调用方法
                    EncapsulateClass.adjustVolume(MainActivity.this, i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarVolume.setMax(EncapsulateClass.getVolumeMax(MainActivity.this));
        seekBarVolume.setProgress(EncapsulateClass.getCurrentVolume(MainActivity.this));
        dialog.setContentView(inflate);
        dialog.show();
    }
}
