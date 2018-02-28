package com.example.songmachine.ui.welcome;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.songmachine.MainActivity;
import com.example.songmachine.R;
import com.example.songmachine.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private List<String> permissionsList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                            permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("liu","App拥有读取sd权限");
                        Intent intent = new Intent();
                        intent.setClass(GuideActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        if (Build.VERSION.SDK_INT >= 23) {
            checkAppPermission();
        }
    }

    /**
     * 检查App权限
     */
    private void checkAppPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            Log.e("liu","App拥有读取sd权限1");
            Intent intent = new Intent();
            intent.setClass(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (!permissionsList.isEmpty()) {
            String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
            ActivityCompat.requestPermissions(GuideActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            MethodUtil.toast(GuideActivity.this, getString(R.string.ui_welcome_request_permission_text));
                            finish();
                        }
                    }
                }
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                break;
        }
    }
}
