package com.example.floatwindow;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class FloatWindowActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnFloatWindow;
    public static final int STOP_SERVICE = 1;
    public static final int START_SERVICE = 2;
    public Message message;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP_SERVICE:
                    Intent intent = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                    stopService(intent);
                    break;
                case START_SERVICE:
                    Intent intent1 = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                    startService(intent1);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_window);

        btnFloatWindow = (Button) this.findViewById(R.id.create_float_window);
        btnFloatWindow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_float_window:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                        return;
                    } else {
                        Intent intent = new Intent(FloatWindowActivity.this, FloatWindowService.class);
                        startService(intent);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MENU) {
            message = new Message();
            message.what = STOP_SERVICE;
            handler.sendMessage(message);
        }
        return super.onKeyDown(keyCode, event);
    }
}
