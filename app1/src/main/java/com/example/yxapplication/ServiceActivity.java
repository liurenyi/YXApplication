package com.example.yxapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ServiceActivity extends AppCompatActivity {

    public static final String TAG = "liu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Intent intent = new Intent(ServiceActivity.this, MyService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("liu","onServiceConnected()");
            MyService.MyBinder binder = (MyService.MyBinder) iBinder;
            MyService service = binder.getService();
            service.testMethod("hi,service");
            service.setListener(new MyService.OnTestListener() {
                @Override
                public void onData(String string) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i("liu","onServiceDisconnected()");
        }
    };

}
