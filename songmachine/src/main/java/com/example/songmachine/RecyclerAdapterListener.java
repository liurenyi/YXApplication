package com.example.songmachine;

import android.view.View;

/**
 * Created by Administrator on 2018/1/26.
 */

public class RecyclerAdapterListener {

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

}
