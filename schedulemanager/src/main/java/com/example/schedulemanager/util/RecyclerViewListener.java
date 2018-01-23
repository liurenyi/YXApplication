package com.example.schedulemanager.util;

import android.view.View;

/**
 * Created by Administrator on 2018/1/22.
 */

public class RecyclerViewListener {

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position);
    }

}
