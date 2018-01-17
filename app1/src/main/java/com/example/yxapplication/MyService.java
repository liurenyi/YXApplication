package com.example.yxapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public static String TAG = "liu";

    public OnTestListener listener;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        public MyService getService() {
            return MyService.this;
        }

    }

    public void testMethod(String string) {
        Log.i("liu", "testMethod: " + string);
        Log.i("liu","listener: " + listener);
        if (this.listener != null) {
            listener.onData(".....");
        }
    }

    public interface OnTestListener {
        void onData(String string);
    }

    public void setListener(OnTestListener listener) {
        this.listener = listener;
    }
}
