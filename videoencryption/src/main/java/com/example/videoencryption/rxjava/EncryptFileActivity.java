package com.example.videoencryption.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.videoencryption.CStorageManager;
import com.example.videoencryption.R;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EncryptFileActivity extends AppCompatActivity {

    private static final String TAG = "liu-EncryptFileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_file);
        initRXJava();
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
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

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
                    }
                });
    }
}
