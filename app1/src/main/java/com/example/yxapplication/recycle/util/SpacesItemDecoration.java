package com.example.yxapplication.recycle.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/1/19.
 */

/**
 * 线性布局的间距封装类
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int spcae;
    private boolean IS_HORIZONTAL; // 是否是水平方向

    public SpacesItemDecoration(int spcae, boolean b) {
        this.spcae = spcae;
        this.IS_HORIZONTAL = b;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (IS_HORIZONTAL) {
            outRect.left = spcae;
        } else {
            outRect.top = spcae;
        }

    }
}
