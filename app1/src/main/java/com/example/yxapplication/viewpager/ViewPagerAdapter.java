package com.example.yxapplication.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> views;

    public ViewPagerAdapter(Context mContext, List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("liu", "--destroyItem()");
        //super.destroyItem(container, position, object);
        container.removeView(views.get(position % views.size()));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i("liu", "--instantiateItem()");
        //container.removeAllViews();

        // container.addView(views.get(position));
        container.addView(views.get(position % views.size()));
        return views.get(position % views.size());
    }
}
