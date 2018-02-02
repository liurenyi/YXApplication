package com.example.videoencryption;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

import com.example.videoencryption.rxjava.RxJavaPackage;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    public static final String TAG = "liu-MainActivity";
    private VideoView videoMain;
    private FileDES fileDES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoMain = (VideoView) this.findViewById(R.id.video_main);
        try {
            fileDES = new FileDES("yuxinkeji.liu");
            //fileDES.doEncryptFile("/mnt/sdcard/1234.mkv", "/mnt/sdcard/4567.mkv");
            String path = CStorageManager.getInnerSDCardPath();
            //fileDES.encrypt(path + "/1234.mkv");
            Log.e(TAG, "path: " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initVideo();
        //initRx();
        File path = new File(CStorageManager.getInnerSDCardPath()); // 获取内部存储根目录
        RxJavaPackage.ReadFileFromRXJava(path);
    }



    private void initRx() {
        File path = new File(CStorageManager.getInnerSDCardPath()); // 获取内部存储根目录
        Observable.just(path).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return RxUtils.listFiles(file);
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "-->onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File file) {
                        String name = file.getName();
                        long length = file.length();
                        String filePath = file.getPath();
                        Log.e(TAG, "name: " + name);
                        Log.e(TAG, "length: " + length);
                        Log.e(TAG, "filePath: " + filePath);
                    }
                });

    }

    //subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    private void initVideo() {
        Uri uri = Uri.parse("/mnt/sdcard/1234.mkv");
        videoMain.setVideoURI(uri);
        videoMain.setOnPreparedListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoMain.start();
    }
}
