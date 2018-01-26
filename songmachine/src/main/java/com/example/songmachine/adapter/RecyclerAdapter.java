package com.example.songmachine.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songmachine.R;
import com.example.songmachine.RecyclerAdapterListener;
import com.example.songmachine.log.Logw;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/25.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Map<Object, Object>> mapList;
    private RecyclerAdapterListener.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyclerAdapterListener.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerAdapter(Context context, List<Map<Object, Object>> mapList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mapList = mapList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imgVideo = view.findViewById(R.id.img_video_info);
        viewHolder.textView1 = view.findViewById(R.id.textView);
        viewHolder.textView2 = view.findViewById(R.id.textView2);
        viewHolder.textView3 = view.findViewById(R.id.textView3);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Logw.i("liu", "-------");
        holder.imgVideo.setImageBitmap((Bitmap) mapList.get(position).get("image"));
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView imgVideo;
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}
