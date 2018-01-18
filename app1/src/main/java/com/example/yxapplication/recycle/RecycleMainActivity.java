package com.example.yxapplication.recycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yxapplication.R;
import com.example.yxapplication.recycle.adapter.GalleryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleMainActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public GalleryAdapter galleryAdapter;
    public List<Integer> mDatas;
    public ImageView imgShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*隐藏掉状态栏，透明化状态只需要在style中定义windowTranslucentStatus为false即可*/
        /*Window window = getWindow();
        int fullscreen = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(fullscreen, fullscreen);*/
        setContentView(R.layout.activity_recycle_main);
        initDatas();
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        imgShow = (ImageView) this.findViewById(R.id.show_img);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        galleryAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(galleryAdapter);
        galleryAdapter.setOnItemClickListener(new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(RecycleMainActivity.this, "position: " + position, Toast.LENGTH_LONG).show();
                imgShow.setBackgroundResource(mDatas.get(position));
            }
        });
        /**
         * mRecyclerView滚动的监听事件
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: //0（SCROLL_STATE_IDLE）表示recyclerview是不动的
                    case RecyclerView.SCROLL_STATE_DRAGGING: //1（SCROLL_STATE_DRAGGING）表示recyclerview正在被拖拽
                    case RecyclerView.SCROLL_STATE_SETTLING: //2（SCROLL_STATE_SETTLING）表示recyclerview正在惯性下滚动
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition(); //获取可是范围内，第一个item的位置
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition(); //获取可是范围内，最后一个item的位置
                int itemCount = manager.getItemCount();
                Log.i("liu", "firstVisibleItemPosition: " + firstVisibleItemPosition + " lastVisibleItemPosition: " + lastVisibleItemPosition);
                Log.i("liu", "itemCount: " + itemCount + " dx: " + dx + " dy: " + dy);
                imgShow.setBackgroundResource(mDatas.get(firstVisibleItemPosition));
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4,
                R.drawable.image_5, R.drawable.image_6, R.drawable.image_7, R.drawable.image_8,
                R.drawable.image_9, R.drawable.image_10));
    }
}
