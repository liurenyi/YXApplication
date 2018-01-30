package com.example.songmachine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.songmachine.log.Logw;

/**
 * 这个是测试广播收发是否正常的
 * Created by Administrator on 2018/1/30.
 */

public class TestBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logw.e("liu", "action: " + action);
        if (CIntent.ACTION_AGAIN_START.equals(action)) {
            Toast(context);
        }
    }

    private void Toast(Context context) {
        Toast.makeText(context, "接收到了Mainactivity发出来的广播", Toast.LENGTH_SHORT).show();
    }

}
