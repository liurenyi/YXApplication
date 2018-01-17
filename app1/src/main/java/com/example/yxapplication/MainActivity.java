package com.example.yxapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    public ListView listView;
    public ArrayList<Map<String, String>> mLists = new ArrayList<>();
    public MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) this.findViewById(R.id.text);
        listView = (ListView) this.findViewById(R.id.list_item);
        test();
        mAdapter = new MyAdapter(MainActivity.this, mLists);
        listView.setAdapter(mAdapter);
    }

    private void test() {
        for (int i = 0; i <= 100; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "liurenyi");
            map.put("address", "shenzhen");
            mLists.add(map);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("liu", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("liu", "onPause()");
    }
}
