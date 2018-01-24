package com.example.songmachine.log;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/24.
 */

public class Logw {

    public static final boolean DEBUG = true;

    public static Serializable v(String tag, String string) {
        return DEBUG ? Log.v(tag, string) : "debug mode is false";
    }

    public static Serializable d(String tag, String string) {
        return DEBUG ? Log.d(tag, string) : "debug mode is false";
    }

    public static Serializable i(String tag, String string) {
        return DEBUG ? Log.i(tag, string) : "debug mode is false";
    }

    public static Serializable w(String tag, String string) {
        return DEBUG ? Log.w(tag, string) : "debug mode is false";
    }

    public static Serializable e(String tag, String string) {
        return DEBUG ? Log.e(tag, string) : "debug mode is false";
    }
}
