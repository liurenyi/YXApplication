package com.example.songmachine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.songmachine.R;

import java.util.List;
import java.util.Map;

public class SongNumberAdapter extends BaseAdapter {

    private List<Map<String, String>> selectedMapList;
    private LayoutInflater inflater;

    public SongNumberAdapter(Context context, List<Map<String, String>> selectedMapList) {
        inflater = LayoutInflater.from(context);
        this.selectedMapList = selectedMapList;
    }

    @Override
    public int getCount() {
        return selectedMapList.size();
    }

    @Override
    public Object getItem(int i) {
        return selectedMapList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_listview_song_number, null);
            viewHolder.tvSongName = view.findViewById(R.id.tv_song_name);
            viewHolder.tvSongAuthor = view.findViewById(R.id.tv_song_author);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvSongName.setText(selectedMapList.get(i).get("videoName"));
        viewHolder.tvSongAuthor.setText(selectedMapList.get(i).get("videoPath"));
        return view;
    }

    private class ViewHolder {
        TextView tvSongName;
        TextView tvSongAuthor;
    }

}
