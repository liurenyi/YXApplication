package com.example.songmachine.ui.welcome;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.songmachine.R;
import com.example.songmachine.util.MethodUtil;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    public ViewPager viewPager_main;
    public List<View> list;
    private List<String> permissionsList = new ArrayList<>();
    private ImageView[] dots;
    public LinearLayout linearLayout;
    private int[] imgIds = new int[]{R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3,
            R.drawable.welcome4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT >= 23) {
            checkAppPermission();
        }
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            checkAppPermission();
        }
    }

    /**
     * 检查App权限
     */
    private void checkAppPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionsList.isEmpty()) {
            String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
            ActivityCompat.requestPermissions(WelcomeActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            MethodUtil.toast(WelcomeActivity.this, getString(R.string.ui_welcome_request_permission_text));
                            finish();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 初始化
     */
    private void initUI() {
        viewPager_main = (ViewPager) this.findViewById(R.id.viewPager_main);
        list = new ArrayList<>();
        for (int imgId : imgIds) {
            ImageView imgView = new ImageView(this);
            imgView.setImageResource(imgId);
            list.add(imgView);
        }
        viewPager_main.setAdapter(new MyAdapter(list));
        viewPager_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (ImageView dot : dots) {
                    dot.setEnabled(true);
                }
                dots[position].setEnabled(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initDots();
    }

    private void initDots() {
        linearLayout = (LinearLayout) this.findViewById(R.id.layout_main);
        dots = new ImageView[imgIds.length];
        for (int i = 0; i < imgIds.length; i++) {
            dots[i] = (ImageView) linearLayout.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setTag(i);
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //viewPager_main.setCurrentItem((Integer) v.getTag());
                }
            });
            dots[0].setEnabled(false);
        }
    }

    private class MyAdapter extends PagerAdapter {

        private List<View> list;

        private MyAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
