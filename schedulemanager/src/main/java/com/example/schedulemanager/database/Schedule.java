package com.example.schedulemanager.database;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/1/22.
 */

public class Schedule extends DataSupport {

    private int id;
    private String date;
    private String time;
    private String alarmData;
    private String alarmTime;
    private String title;
    private String note;
    private String type;
    private boolean timeset;
    private boolean alarmset;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarmData() {
        return alarmData;
    }

    public void setAlarmData(String alarmData) {
        this.alarmData = alarmData;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTimeset() {
        return timeset;
    }

    public void setTimeset(boolean timeset) {
        this.timeset = timeset;
    }

    public boolean isAlarmset() {
        return alarmset;
    }

    public void setAlarmset(boolean alarmset) {
        this.alarmset = alarmset;
    }
}
