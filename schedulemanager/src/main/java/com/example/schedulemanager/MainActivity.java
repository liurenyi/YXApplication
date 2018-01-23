package com.example.schedulemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.schedulemanager.adapter.ScheduleAdapter;
import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.util.DBUtil;
import com.example.schedulemanager.util.RecyclerViewListener;

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
    public static final int KEY_CREATE_ALTER_DIALOG = 7;
    public static final int KEY_GO_TO_DETAIL_INFO = 8;

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
                case KEY_CREATE_ALTER_DIALOG:
                    createDialog((Integer) msg.obj);
                    break;
                case KEY_GO_TO_DETAIL_INFO:
                    goScheduleDetailActivity((Integer) msg.obj);
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

    /**
     * 日程的详细情况界面
     *
     * @param id
     */
    private void goScheduleDetailActivity(int id) {
        intent = new Intent(MainActivity.this, ScheduleDetailActivity.class);
        intent.putExtra("id", id);
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
        updateUI();
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

    public void createDialog(final int id) {
        new AlertDialog.Builder(MainActivity.this).setTitle("警告").setMessage("是否删除选中数据").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBUtil.deleteData(Schedule.class, id); // 执行删除操作
                // 删除数据之后重新加载下数据
                updateUI();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    /**
     * 更新界面数据
     */
    public void updateUI() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSchedule.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerViewSchedule.setLayoutManager(manager);
        mLists = QueryDb();
        adapter = new ScheduleAdapter(MainActivity.this, mLists);
        recyclerViewSchedule.setAdapter(adapter);
        // item的回调事件
        adapter.setListener(new RecyclerViewListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                message = new Message();
                message.what = KEY_GO_TO_DETAIL_INFO;
                message.obj = mLists.get(position).getId(); //获取当前数据的id,根据id去查询数据库的内容
                handler.sendMessage(message);
            }
        });
        adapter.setLongClickListener(new RecyclerViewListener.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position) {
                message = new Message();
                message.what = KEY_CREATE_ALTER_DIALOG;
                message.obj = mLists.get(position).getId();
                handler.sendMessage(message);
            }
        });
    }
}
