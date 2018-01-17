package com.example.yxapplication.recycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yxapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> mDatas;

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public GalleryAdapter(Context context, List<Integer> mDatas) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_index_gallery_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.imageView = view.findViewById(R.id.id_index_gallery_item_image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setBackgroundResource(mDatas.get(position));
        if (onItemClickListener !=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        ImageView imageView;
        TextView textView;
    }

}
