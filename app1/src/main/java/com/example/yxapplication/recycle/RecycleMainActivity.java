package com.example.yxapplication.recycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        galleryAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(galleryAdapter);
        galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(RecycleMainActivity.this, "position: " + position, Toast.LENGTH_LONG).show();
                imgShow.setBackgroundResource(mDatas.get(position));
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4,
                R.drawable.image_5, R.drawable.image_6, R.drawable.image_7, R.drawable.image_8,
                R.drawable.image_9, R.drawable.image_10));
    }
}
