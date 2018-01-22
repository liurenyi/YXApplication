package com.example.yxapplication.viewpager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.yxapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseViewpagerActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<View> views;
    private ViewPagerAdapter adapter;
    private View view;
    private ImageView image;
    private GridView gridView;
    private List<Map<String, Object>> data_list;
    private int[] imgages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3,
            R.drawable.image_5, R.drawable.image_6, R.drawable.image_7,
            R.drawable.image_8, R.drawable.image_9, R.drawable.image_10};
    private String[] titles = {"美女", "美女", "美女", "美女", "美女", "美女", "美女", "美女", "美女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_viewpager);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
        views = new ArrayList<>();
        data_list = new ArrayList<>();
        initDatas();
        adapter = new ViewPagerAdapter(this, views);
        viewPager.setAdapter(adapter);
    }

    private void initDatas() {
        for (int i = 0; i < imgages.length; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.viewpager_views, null);
            //image = view.findViewById(R.id.img_view);
            //image.setBackgroundResource(imgages[i]);
            gridView = view.findViewById(R.id.grid_view);
            Map<String, Object> map = new HashMap<>();
            map.put("images", imgages[i]);
            map.put("text", titles[i]);
            data_list.add(map);
            String[] from = {"images", "text"};
            int[] to = {R.id.show_img_1, R.id.image_title};
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, data_list, R.layout.grid_view_item, from, to);
            gridView.setAdapter(simpleAdapter);
            views.add(view);
        }
    }
}
