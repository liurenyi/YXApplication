package com.example.schedulemanager.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/22.
 */

public class UtilClass {

    private static Date date = new Date();

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        if (!"".equals(string)) {
            Pattern pattern = Pattern.compile("\\s|\n|\r|\t");//去掉
            Matcher matcher = pattern.matcher(string);
            str = matcher.replaceAll("");
        }
        return str;
    }

}
