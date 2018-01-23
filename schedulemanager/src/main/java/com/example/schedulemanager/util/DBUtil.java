package com.example.schedulemanager.util;

import android.content.ContentValues;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/23.
 */

public class DBUtil {

    /**
     * 删除某一条数据
     *
     * @param modeClass
     * @param id
     */
    public static void deleteData(Class<?> modeClass, long id) {
        DataSupport.delete(modeClass, id);
    }

    /**
     * 查找某一条数据
     *
     * @param modeClass
     * @param id
     */
    public static Object queryData(Class<?> modeClass, long id) {
        return DataSupport.find(modeClass, id);
    }

    /**
     * 更新某一条数据
     *
     * @param modeClass
     * @param values
     * @param id
     */
    public static int updateData(Class<?> modeClass, ContentValues values, long id) {
        return DataSupport.update(modeClass, values, id);
    }

}
