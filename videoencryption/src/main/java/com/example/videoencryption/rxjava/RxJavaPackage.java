package com.example.videoencryption.rxjava;

import android.util.Log;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava的封装类
 * Created by Administrator on 2018/2/2.
 */

public class RxJavaPackage {

    private static final String TAG = "liu-RxJavaPackage";

    /**
     * 遍历某一个路径的下的文件
     *
     * @param file 绝对路径
     */
    public static void ReadFileFromRXJava(File file) {
        Log.e(TAG,"-->ReadFileFromRXJava()");
        Observable.just(file).flatMap(new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                return listFiles(file);
            }
        }).doOnNext(new Action1<File>() {
            @Override
            public void call(File file) {
                Log.e(TAG,"-->doOnNext()");
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
                        Log.e(TAG, "name: " + name);
                    }
                });
    }

    public static Observable<File> listFiles(File file) {
        if (file.isDirectory()) {
            return Observable.from(file.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(file).filter(new Func1<File, Boolean>() { // 过滤出视频文件
                @Override
                public Boolean call(File file) {
                    return file.exists() && file.canRead() && FileJudgment.isVideoFile(file);
                }
            });
        }
    }

}
