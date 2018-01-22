package com.example.schedulemanager.database;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/22.
 */

public class ScheduleType extends DataSupport{

    private int id;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
