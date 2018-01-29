package com.example.schedulemanager.util;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.schedulemanager.AlarmReceiver;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Administrator on 2018/1/29.
 */

public class AlarmRemind {

    public static final String TAG = "AlarmRemind";

    /**
     * 开启提醒
     *
     * @param context
     */
    public static void startRemind(Context context, Dialog dialog, int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long timeMillis = System.currentTimeMillis();
        Log.e("liu","timeMillis: " + timeMillis);
        //是设置日历的时间，主要是让日历的年月日和当前同步
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        //设置在几点提醒  设置的为13点
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        //设置在几分提醒  设置的为25分
        calendar.set(Calendar.MINUTE, minute);
        //下面这两个看字面意思也知道
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis();
        Log.e("liu", "selectTime: " + selectTime);
        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (timeMillis > selectTime) {
            //calendar.add(Calendar.DAY_OF_MONTH, 1);
            Toast.makeText(context, "大哥，你设置的时间有问题", Toast.LENGTH_SHORT).show();
            return;
        } else {
            long distanceTime = (selectTime - timeMillis) / 1000;
            Log.e("liu", "distanceTime: " + distanceTime);
            if (distanceTime > 0 && distanceTime < 60) {
                Toast.makeText(context, "提醒在" + distanceTime + "秒后执行", Toast.LENGTH_SHORT).show();
            } else if (distanceTime >= 60 && distanceTime < 3600) {
                Toast.makeText(context, "提醒在" + distanceTime / 60 + "分钟后执行", Toast.LENGTH_SHORT).show();
            } else if (distanceTime >= 3600) {
                Toast.makeText(context, "提醒在" + distanceTime / 3600 + "小时后执行", Toast.LENGTH_SHORT).show();
            }
        }

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值
         */
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

        /**
         * 重复提醒
         * 第一个参数是警报类型；下面有介绍
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
        //am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);

        dialog.dismiss(); // 设置完成之后弹出框消失
    }

    /**
     * 关闭提醒
     */
    private void stopRemind(Context context) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        Toast.makeText(context, "关闭了提醒", Toast.LENGTH_SHORT).show();

    }

}
