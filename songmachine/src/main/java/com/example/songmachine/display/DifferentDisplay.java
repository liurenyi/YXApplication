package com.example.songmachine.display;

import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.songmachine.CIntent;
import com.example.songmachine.R;
import com.example.songmachine.log.Logw;
import com.example.songmachine.util.StorageCManager;

/**
 * 这个类关于副屏幕显示的内容，可在此操作。
 * 广播好像接收不过来
 * Created by Administrator on 2018/1/26.
 */

public class DifferentDisplay extends Presentation implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private VideoView vvSecondScreen;
    private Context context;
    private static Message message;
    private static final int KEY_ACTION_AGAIN_START = 1;
    private static final int KEY_ACTION_MUTE = 2;
    private static final int KEY_ACTION_PAUSE = 3;
    private static final int KEY_ACTION_VOLUME = 4;
    private static final int KEY_ACTION_SWITCH_SONG = 5;
    private static final int KEY_ACTION_ACCOMPANIMENT = 6;
    private static final int KEY_ACTION_ATMOSPHERE = 7;

    public DifferentDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY_ACTION_AGAIN_START:
                    test();
                    break;
            }
        }
    };

    /**
     * 测试方法，测试广播是否通了
     */
    private void test() {
        Logw.e("liu","................test"); // 打印不出log
        Toast.makeText(context,"hahaha",Toast.LENGTH_SHORT).show();
        vvSecondScreen.seekTo(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.different_dispaly);
        Logw.e("liu","................test");
        init();
        registerBroadcast();
    }

    private void init() {
        vvSecondScreen = this.findViewById(R.id.video_second_screen);
        // 此处使用的Uri应是主屏幕正在播放的视频的Uri,保持此处的uri与主屏幕同步。达到双屏异显功能
        Uri uri = Uri.parse(StorageCManager.filePath); // 测试Uri
        vvSecondScreen.setVideoURI(uri);
        vvSecondScreen.setOnPreparedListener(this);
        vvSecondScreen.setOnCompletionListener(this);
        vvSecondScreen.setOnErrorListener(this);
    }

    /**
     * 注册广播事件
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CIntent.ACTION_AGAIN_START);
        filter.addAction(CIntent.ACTION_ACCOMPANIMENT);
        filter.addAction(CIntent.ACTION_ATMOSPHERE);
        filter.addAction(CIntent.ACTION_MUTE);
        filter.addAction(CIntent.ACTION_PAUSE);
        filter.addAction(CIntent.ACTION_SWITCH_SONG);
        filter.addAction(CIntent.ACTION_VOLUME);
        context.registerReceiver(receiver, filter);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Logw.e("liu", "--->onCompletion()");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Logw.e("liu", "--->onError()");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Logw.e("liu", "--->onPrepared()");
        vvSecondScreen.start();
    }

    /**
     * 广播事件，主屏幕点击静音，音量加，气氛等功能时候，此时接到指令进行相应的操作
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (CIntent.ACTION_ACCOMPANIMENT.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_AGAIN_START;
                handler.sendMessage(message);
            } else if (CIntent.ACTION_AGAIN_START.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_MUTE;
                handler.sendMessage(message);
            } else if (CIntent.ACTION_ATMOSPHERE.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_PAUSE;
                handler.sendMessage(message);
            } else if (CIntent.ACTION_MUTE.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_VOLUME;
                handler.sendMessage(message);
            } else if (CIntent.ACTION_PAUSE.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_SWITCH_SONG;
                handler.sendMessage(message);
            } else if (CIntent.ACTION_SWITCH_SONG.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_ACCOMPANIMENT;
                handler.sendMessage(message);
            } else if (CIntent.ACTION_VOLUME.equals(action)) {
                message = new Message();
                message.what = KEY_ACTION_ATMOSPHERE;
                handler.sendMessage(message);
            }
        }
    };

    @Override
    public void onDisplayRemoved() {
        super.onDisplayRemoved();
        context.unregisterReceiver(receiver); // 解除注册
    }
}
