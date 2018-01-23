package com.example.schedulemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.util.DBUtil;
import com.example.schedulemanager.util.UtilClass;

public class ScheduleDetailActivity extends AppCompatActivity {

    public TextView tvScheduleTime;
    public EditText edScheduleRemarks;
    public ImageView imgOK;
    public ImageView imgCancle;
    private int id;
    private String stringFromDB; // 数据库中的备注信息
    private String stringCurrent; // 此时EditText的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        tvScheduleTime = (TextView) this.findViewById(R.id.tv_schedule_created_time);
        edScheduleRemarks = (EditText) this.findViewById(R.id.edit_schedule_remark_info);
        imgOK = (ImageView) this.findViewById(R.id.img_save);
        imgCancle = (ImageView) this.findViewById(R.id.img_back);
        imgOK.setVisibility(View.GONE);
        imgOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("note", stringCurrent);
                values.put("date", UtilClass.getCurrentDate());
                values.put("time", UtilClass.getCurrentTime());
                DBUtil.updateData(Schedule.class, values, id); // 更新数据
                finish();
            }
        });

        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edScheduleRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stringCurrent = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 如果数据库中的数据和此时的数据不同，出现保存的选项，是数据可以保存下来
                if (!stringFromDB.equals(editable.toString())) {
                    imgOK.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = this.getIntent();
        id = intent.getIntExtra("id", -1);
        if (id == -1) return;
        Schedule schedule = (Schedule) DBUtil.queryData(Schedule.class, id);
        String time = schedule.getTime();
        String date = schedule.getDate();
        String remarks = schedule.getNote();
        stringFromDB = remarks;
        // 如果此数据库中得到的数据为空，则对其赋值为""。
        if (stringFromDB == null) {
            stringFromDB = "";
        }
        tvScheduleTime.setText(date + " " + time);
        edScheduleRemarks.setText(remarks);
    }
}
