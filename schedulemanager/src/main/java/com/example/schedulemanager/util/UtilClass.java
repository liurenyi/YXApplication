package com.example.schedulemanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.example.schedulemanager.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/22.
 */

public class UtilClass {

    private static Date date = new Date();
    private static Calendar calendar = Calendar.getInstance();

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(date);
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     *
     * @param string
     * @return
     */
    public static String getFormatString(String string) {
        String str = "";
        if (string != null) {
            Pattern pattern = Pattern.compile("\\s|\n|\r|\t");//去掉
            Matcher matcher = pattern.matcher(string);
            str = matcher.replaceAll("");
        }
        return str;
    }


    /**
     * 定义状态栏的颜色
     *
     * @param activity
     */
    public static void initSystemBar(Activity activity) {
        /**
         * 4.4版本以上开启
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);
        //tintManager.setNavigationBarTintResource(R.color.colorWhite); // 无颜色出来,自定义的颜色
        //tintManager.setTintColor(R.color.); // 无颜色出来 ???
        //tintManager.setStatusBarTintResource(R.color.colorWhite); // 无颜色出来 ??? 设置为白色有毒，不显示，还是那个阴影
        tintManager.setTintColor(Color.parseColor("#00FFccFF"));
        //tintManager.setStatusBarAlpha(255);// 0全部透明
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置分割线的颜色
     *
     * @param datePicker
     */
    private void setDatePickerDividerColor(DatePicker datePicker, Context context) {
        // Divider changing:
        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] declaredFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : declaredFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    public static int getNowYear() {
        return calendar.get(Calendar.YEAR);
    }

    public static int getNowMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public static int getNowDat() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getNowDate() {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return "星期日";
        } else if (dayOfWeek == 2) {
            return "星期一";
        } else if (dayOfWeek == 3) {
            return "星期二";
        } else if (dayOfWeek == 4) {
            return "星期三";
        } else if (dayOfWeek == 5) {
            return "星期四";
        } else if (dayOfWeek == 6) {
            return "星期五";
        } else if (dayOfWeek == 7) {
            return "星期六";
        } else {
            return null;
        }
    }

    /**
     * 获取指定的日期是星期几
     *
     * @return
     */
    public static String getAppointedDate(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return "星期日";
        } else if (dayOfWeek == 2) {
            return "星期一";
        } else if (dayOfWeek == 3) {
            return "星期二";
        } else if (dayOfWeek == 4) {
            return "星期三";
        } else if (dayOfWeek == 5) {
            return "星期四";
        } else if (dayOfWeek == 6) {
            return "星期五";
        } else if (dayOfWeek == 7) {
            return "星期六";
        } else {
            return null;
        }
    }

}
