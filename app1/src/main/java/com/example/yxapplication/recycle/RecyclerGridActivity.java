package com.example.yxapplication.recycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yxapplication.R;
import com.example.yxapplication.recycle.adapter.GalleryAdapter;
import com.example.yxapplication.recycle.util.GridSpacingItemDecoration;
import com.example.yxapplication.recycle.util.UtilCalss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerGridActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public List<Integer> mDatas;
    public GalleryAdapter galleryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_grid);
        initDatas();
        int width = UtilCalss.getWidth(this);
        int height = UtilCalss.getHeight(this);
        Log.d("liu", "width: " + width + " height: " + height);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_grid_view);
        //GridLayoutManager manager = new GridLayoutManager(this, 3);
        //manager.setOrientation(GridLayoutManager.HORIZONTAL);
        //mRecyclerView.setLayoutManager(manager);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        galleryAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(galleryAdapter);
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 15, true)); // 设置网格之间的间隔
        mRecyclerView.setHasFixedSize(true);
        galleryAdapter.setOnItemClickListener(new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(RecyclerGridActivity.this, "position: " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.image_1, R.drawable.image_2, R.drawable.image_3,
                R.drawable.image_5, R.drawable.image_6, R.drawable.image_7, R.drawable.image_8,
                R.drawable.image_9, R.drawable.image_10));
    }
}
