package com.example.schedulemanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.schedulemanager.adapter.ScheduleAdapter;
import com.example.schedulemanager.database.Schedule;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnAddSchedule;
    public Button btnDeleteSchedule;
    public Button btnModifySchedule;
    public Button btnQuerySchedule;
    public Button btnDeleteExpireSchedule;
    public Button btnMaintainSchedule;

    public RecyclerView recyclerViewSchedule;
    public List<Schedule> mLists;
    public ScheduleAdapter adapter;

    private Message message;
    private Intent intent;

    public static final int KEY_ADD_SCHEDULE = 1;
    public static final int KEY_DELETE_SCHEDULE = 2;
    public static final int KEY_MODIFY_SCHEDULE = 3;
    public static final int KEY_QUERY_SCHEDULE = 4;
    public static final int KEY_DELETE_EXPIRE_SCHEDULE = 5;
    public static final int KEY_MAINTAIN_SCHEDULE = 6;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEY_ADD_SCHEDULE:
                    gotoAddSchedule();
                    break;
                case KEY_DELETE_SCHEDULE:
                    break;
            }
        }
    };

    /**
     * 新建日程管理
     */
    private void gotoAddSchedule() {
        intent = new Intent(MainActivity.this, AddScheduleActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        SQLiteDatabase database = Connector.getDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSchedule.addItemDecoration( new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerViewSchedule.setLayoutManager(manager);
        mLists = QueryDb();
        adapter = new ScheduleAdapter(MainActivity.this, mLists);
        recyclerViewSchedule.setAdapter(adapter);
    }

    private void initUI() {
        btnAddSchedule = (Button) this.findViewById(R.id.btn_add_schedule);
        btnDeleteSchedule = (Button) this.findViewById(R.id.btn_delete_schedule);
        btnModifySchedule = (Button) this.findViewById(R.id.btn_modify_schedule);
        btnQuerySchedule = (Button) this.findViewById(R.id.btn_query_schedule);
        btnDeleteExpireSchedule = (Button) this.findViewById(R.id.btn_delete_expire_schedule);
        btnMaintainSchedule = (Button) this.findViewById(R.id.btn_maintain_schedule);
        btnAddSchedule.setOnClickListener(this);
        btnDeleteSchedule.setOnClickListener(this);
        btnModifySchedule.setOnClickListener(this);
        btnQuerySchedule.setOnClickListener(this);
        btnDeleteExpireSchedule.setOnClickListener(this);
        btnMaintainSchedule.setOnClickListener(this);

        recyclerViewSchedule = (RecyclerView) this.findViewById(R.id.list_schedule);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_schedule:
                message = new Message();
                message.what = KEY_ADD_SCHEDULE;
                handler.sendMessage(message);
                break;
            case R.id.btn_delete_schedule:
                break;
            case R.id.btn_delete_expire_schedule:
                break;
            case R.id.btn_maintain_schedule:
                break;
            case R.id.btn_modify_schedule:
                break;
            case R.id.btn_query_schedule:
                break;
        }
    }

    public List<Schedule> QueryDb() {
        /*List<Schedule> schedules = DataSupport.findAll(Schedule.class);
        for (Schedule schedule : schedules) {
            String title = schedule.getTitle();
            Log.i("liu","title: " + title);
        }*/
        return DataSupport.findAll(Schedule.class);
    }
}
