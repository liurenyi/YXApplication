package com.example.videoencryption.rxjava;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.videoencryption.CStorageManager;
import com.example.videoencryption.FileDES;
import com.example.videoencryption.R;

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

public class EncryptFileActivity extends AppCompatActivity {

    private static final String TAG = "liu-EncryptFileActivity";
    private FileDES fileDES;
    public VideoView videoView;
    private List<Map<String, String>> lists = new ArrayList<>();
    private Map<String, String> map;
    private String mCurrentPlayVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_file);
        initRXJava();
        try {
            fileDES = new FileDES("yuxinkeji.liu");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        videoView = (VideoView) findViewById(R.id.video_main);
        if (isEncrypt(lists.get(4).get("name"))) {
            fileDES.encrypt(lists.get(4).get("filePath")); //解密工作
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(EncryptFileActivity.this).edit();
            editor.putBoolean(lists.get(4).get("name"), false);
            editor.apply();
            Uri uri = Uri.parse(lists.get(4).get("filePath"));
            mCurrentPlayVideoPath = lists.get(4).get("filePath");
            videoView.setVideoURI(uri);
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.e(TAG, "-->onPrepared()");
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.e(TAG, "-->onError()");
                return false;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.e(TAG, "-->onCompletion()");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 播放完成加密
                        if (!isEncrypt(lists.get(4).get("name"))) {
                            fileDES.encrypt(lists.get(4).get("filePath"));
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(EncryptFileActivity.this).edit();
                            editor.putBoolean(lists.get(4).get("name"), true);
                            editor.apply();
                        }
                    }
                }).start();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlayback();
        // 当失去焦点，播放停止，并且对视频进行加密处理
        if (!isEncrypt(lists.get(4).get("name"))) {
            fileDES.encrypt(lists.get(4).get("filePath"));
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(EncryptFileActivity.this).edit();
            editor.putBoolean(lists.get(4).get("name"), true);
            editor.apply();
        }
    }

    private void initRXJava() {
        File f = new File(CStorageManager.getInnerSDCardPath());
        Observable.just(f).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return RxJavaPackage.listFiles(file);
            }
        }).doOnNext(new Action1<File>() {
            @Override
            public void call(File file) {
                String name = file.getName();
                String filePath = file.getPath();
                String videoLength = FileJudgment.formatVideoLength(file.length());
                if (!isEncrypt(name)) {
                    fileDES.encrypt(filePath);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(EncryptFileActivity.this).edit();
                    editor.putBoolean(name, true);
                    editor.apply();
                } else {
                    Log.e(TAG, name + "文件已加密");
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File file) {
                        String name = file.getName();
                        String filePath = file.getPath();
                        String videoLength = FileJudgment.formatVideoLength(file.length());
                        Log.e(TAG, "name: " + name + " filePath: " + filePath + " " + " videoLength: " + videoLength);
                        map = new HashMap<>();
                        map.put("name", name);
                        map.put("filePath", filePath);
                        lists.add(map);
                    }
                });
    }

    /**
     * 返回值为true，则表示加密；否则表示不加密
     *
     * @param filename
     * @return
     */
    private boolean isEncrypt(String filename) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EncryptFileActivity.this);
        return preferences.getBoolean(filename, false);
    }
}
