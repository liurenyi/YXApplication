package com.example.yxapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    ArrayList<Map<String, String>> mLists;

    public MyAdapter(Context context, ArrayList<Map<String, String>> lists) {
        this.mContext = context;
        this.mLists = lists;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_item, null);
            holder.name = view.findViewById(R.id.name);
            holder.address = view.findViewById(R.id.address);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(mLists.get(i).get("name"));
        holder.address.setText(mLists.get(i).get("address"));
        return view;
    }

    private class ViewHolder {
        private TextView name;
        private TextView address;
    }
}
