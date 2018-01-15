package com.example.floatwindow.util;

/**
 * Created by Administrator on 2018/1/15.
 */

public class EncapsulateClass {

    public static final String TAG = "EncapsulateClass";
    public static final int A_HOUR = 3600 * 1000;
    public static final int A_MINUTE = 60 * 1000;
    public static final int A_SECOND = 1000;

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        int hour;
        int minute;
        int second;
        if (time >= A_HOUR) {
            hour = (int) (time / (A_HOUR));
            minute = (int) ((time - (hour * A_HOUR)) / A_MINUTE);
            second = (int) ((time - hour * A_HOUR - minute * A_MINUTE) / A_SECOND);
            return hour + ":" + minute + ":" + second;
        } else if (time < A_HOUR && time >= A_MINUTE) {
            minute = (int) (time / A_MINUTE);
            second = (int) ((time - minute * A_MINUTE) / A_SECOND);
            return minute + ":" + second;
        } else if (time>=0 && time < 10 * 1000) {
            second = (int) (time / A_SECOND);
            return "00:0" + second;
        } else {
            second = (int) (time / A_SECOND);
            return "00:" + second;
        }
    }

}
