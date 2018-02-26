package com.example.videoencryption.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.videoencryption.Main2Activity;
import com.example.videoencryption.R;
import com.example.videoencryption.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    private List<String> mPermissions = new ArrayList<>(); // mPermissions只包含必须要的权限,其他权限可去动态申请
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        } else {
            intent = new Intent(PermissionActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 检查App是否拥有权限
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        // 没有权限则去动态请求权限
        if (!mPermissions.isEmpty()) {
            String[] permission = mPermissions.toArray(new String[mPermissions.size()]);
            ActivityCompat.requestPermissions(PermissionActivity.this, permission, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            MethodUtil.toast(PermissionActivity.this, "必须同意此权限才能正常使用App");
                            finish();
                        } else {
                            intent = new Intent(PermissionActivity.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                break;
        }
    }
}
