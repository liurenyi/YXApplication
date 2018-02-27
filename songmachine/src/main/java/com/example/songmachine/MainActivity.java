package com.example.songmachine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.media.MediaMetadataRetriever;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.songmachine.adapter.RecyclerAdapter;
import com.example.songmachine.display.DifferentDisplay;
import com.example.songmachine.util.EncapsulateClass;
import com.example.songmachine.util.StorageCManager;

import java.io.File;
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

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, View.OnClickListener {

    private List<String> mPermissions = new ArrayList<>(); // mPermissions只包含必须要的权限,其他权限可去动态申请
    private static final int KEY_REQUEST_PERMISSION_CODE = 1;
    private static final int KEY_MAIN_VIDEO_PLAYING = 2;
    private static final int KEY_MAIN_VIDEO_PAUSE = 3;
    private static final int KEY_MAIN_VIDEO_REPLAY = 4; // 重唱指令
    private static final int KEY_UPDATE_ITEM = 5;
    private static final int KEY_START_FLOATWINDOWS = 6;
    private static final int KEY_SHOW_VOLUME_UI = 7; // 调节音量指令

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
    public RecyclerView mRecyclerView;

    private RecyclerAdapter adapter;
    private Bitmap bitmap;
    private Message message;
    private Intent intent;
    private DisplayManager mDisplayManager;
    private Display[] mDisplays;
    private DifferentDisplay mDifferentDislay;

    private List<Map<Object, Object>> mapList = new ArrayList<>();
    private List<String> allFilePaths = new ArrayList<>(); // 扫描得到所有视频文件的路径集合

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
                    EncapsulateClass.addVolume(MainActivity.this); // 增加音量的方法，这里调用系统的音量。有需要可自定义音量条
                    break;

            }
        }
    };

    // 此条线程是发送重唱指令
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(CIntent.ACTION_AGAIN_START);
            sendBroadcast(intent);
            Log.e("liu", " 发送测试广播");
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

    /**
     * 重新开始唱歌,主屏副屏同步
     */
    private void goReplay() {
        mVideoVie.seekTo(0); // 主屏播放界面重唱
        if (mDifferentDislay != null) {
            mDifferentDislay.reset(); // 副屏播放界面重唱
        }
    }

    /**
     * 开始播放视频,主屏副屏同步
     */
    private void goPlaying() {
        if (!mVideoVie.isPlaying() && mVideoVie != null) {
            mVideoVie.start();
            mVideoVie.setBackground(null);
            if (mDifferentDislay != null) {
                mDifferentDislay.play();
            }
        }
    }

    /**
     * 暂停视频,主屏副屏同步
     */
    private void goPause() {
        if (mVideoVie != null) {
            mVideoVie.pause();
        }
        if (mDifferentDislay != null) {
            mDifferentDislay.pause();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRxJava();
        initData();
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

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<File>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(File file) {
                String path = file.getPath();
                Log.e("liu","path: " + path);
                allFilePaths.add(path);
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
        Log.e("liu", "all_file_path:" + allFilePaths.toString());
        Uri uri = Uri.parse("/storage/emulated/0/Movies/爱情留在回忆里.mp4"); // 测试路径
        //Uri uri = Uri.parse(allFilePaths.get(0)); // 获取扫描结果的第一个视频文件路径
        createVideoThumbnail("/storage/emulated/0/Movies/爱情留在回忆里.mp4");
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

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new RecyclerAdapter(MainActivity.this, mapList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 15, true));
        adapter.setOnItemClickListener(new RecyclerAdapterListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //Toast.makeText(MainActivity.this, "第" + (position + 1) + "个", Toast.LENGTH_SHORT).show();
                /*Toast.makeText(MainActivity.this, getString(R.string.adapter_click_alert_text, (position + 1) + ""),
                 Toast.LENGTH_SHORT).show();*/
                message = new Message();
                message.what = KEY_START_FLOATWINDOWS;
                handler.sendMessage(message);
            }
        });
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
        handler.postDelayed(runnable, 5000); // 做下测试的指令
        //mVideoVie.setBackground(new BitmapDrawable(bitmap)); // 获取要播放的视频的第一帧
    }

    /**
     * 视频播放完成的监听事件
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //mVideoVie.stopPlayback();
        mVideoVie.start();
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
                /*mRadioButton4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().
                getDrawable(R.drawable.ic_play_arrow), null, null);*/
                mRadioButton4.setCompoundDrawablesWithIntrinsicBounds(null,
                        mVideoVie.isPlaying() ? getResources().getDrawable(R.drawable.ic_play_arrow) :
                                getResources().getDrawable(R.drawable.ic_pause_circle_outline), null, null);
                mRadioButton4.setText(mVideoVie.isPlaying() ? getResources().getString(R.string.radio_button_left_4_text_1) :
                        getResources().getString(R.string.radio_button_left_4_text));
                handler.sendMessage(message);
                break;
            case R.id.radio_left_5:
                message = new Message();
                message.what = KEY_SHOW_VOLUME_UI;
                handler.sendMessage(message);
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

    /**
     * 做测试专用的数据
     */
    private void initData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Map<Object, Object> map = new HashMap<>();
                    map.put("image", StorageCManager.createVideoThumbnail(StorageCManager.filePath));
                    mapList.add(map);
                    if (i % 9 == 0) {
                        message = new Message();
                        message.what = KEY_UPDATE_ITEM;
                        handler.sendMessage(message);
                    }
                }
            }
        }).start();

    }

}
