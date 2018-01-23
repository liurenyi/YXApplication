package com.example.schedulemanager.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedulemanager.R;
import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.util.RecyclerViewListener;
import com.example.schedulemanager.util.UtilClass;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Schedule> mLists;
    public RecyclerViewListener.OnItemClickListener listener;
    public RecyclerViewListener.OnItemLongClickListener longClickListener;


    public void setLongClickListener(RecyclerViewListener.OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setListener(RecyclerViewListener.OnItemClickListener listener) {
        this.listener = listener;
    }

    public ScheduleAdapter(Context context, List<Schedule> mLists) {
        inflater = LayoutInflater.from(context);
        this.mLists = mLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_schedule_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.date = view.findViewById(R.id.tv_date);
        viewHolder.time = view.findViewById(R.id.tv_time);
        viewHolder.title = view.findViewById(R.id.tv_schedule_info);
        viewHolder.type = view.findViewById(R.id.tv_schedule_type);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.date.setText(mLists.get(position).getDate());
        holder.time.setText(mLists.get(position).getTime());
        holder.type.setText("[ " + mLists.get(position).getType() + " ]");
        holder.title.setText(UtilClass.getFormatString(mLists.get(position).getTitle()));
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(view, position);
                }
            });
        }
        //长按点击事件
        if (longClickListener!= null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.OnItemLongClick(view,position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        TextView date;
        TextView time;
        TextView title;
        TextView type;
    }
}
