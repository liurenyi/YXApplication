package com.example.songmachine;

/**
 * 自定义广播事件Action的类
 * C表示custom，自定义的。
 * Created by Administrator on 2018/1/29.
 */

public class CIntent {

    public static final String ACTION_AGAIN_START = "action.again.start"; // 重唱的指令
    public static final String ACTION_MUTE = "action.mute"; // 静音的指令
    public static final String ACTION_PAUSE = "action.pause"; // 暂停的指令
    public static final String ACTION_VOLUME = "action.volume"; // 声音的指令
    public static final String ACTION_SWITCH_SONG = "action.switch.song"; // 切歌的指令
    public static final String ACTION_ACCOMPANIMENT = "action.accompaniment"; // 伴唱的指令
    public static final String ACTION_ATMOSPHERE = "action.atmosphere"; // 气氛的指令

}
