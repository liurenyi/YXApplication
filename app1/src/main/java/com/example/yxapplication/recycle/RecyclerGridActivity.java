package com.example.yxapplication.recycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.yxapplication.R;
import com.example.yxapplication.recycle.adapter.GalleryAdapter;

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
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_grid_view);
        //GridLayoutManager manager = new GridLayoutManager(this, 3);
        //manager.setOrientation(GridLayoutManager.HORIZONTAL);
        //mRecyclerView.setLayoutManager(manager);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        galleryAdapter = new GalleryAdapter(this, mDatas);
        mRecyclerView.setAdapter(galleryAdapter);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        galleryAdapter.setOnItemClickListener(new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(RecyclerGridActivity.this, "position: " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<>(Arrays.asList(R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4,
                R.drawable.image_5, R.drawable.image_6, R.drawable.image_7, R.drawable.image_8,
                R.drawable.image_9, R.drawable.image_10));
    }
}
