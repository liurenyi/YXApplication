package com.example.schedulemanager;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.schedulemanager.adapter.ScheduleAdapter;
import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.util.DBUtil;
import com.example.schedulemanager.util.RecyclerViewListener;
import com.example.schedulemanager.util.UtilClass;
import com.example.yxapplication.recycle.util.GridSpacingItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnAddSchedule;
    public Button btnDeleteSchedule;
    public Button btnModifySchedule;
    public Button btnQuerySchedule;
    public Button btnDeleteExpireSchedule;
    public Button btnMaintainSchedule;
    public RadioButton radioAddSchedule;
    public ImageView imgIsGrid;
    public ImageView imgVolumeDown, imgVolumeUp;
    public ImageView imgTest;


    public RecyclerView recyclerViewSchedule;
    public List<Schedule> mLists;
    public ScheduleAdapter adapter;

    private Message message;
    private Intent intent;
    private boolean layoutIsGrid;

    public static final int KEY_ADD_SCHEDULE = 1;
    public static final int KEY_DELETE_SCHEDULE = 2;
    public static final int KEY_MODIFY_SCHEDULE = 3;
    public static final int KEY_QUERY_SCHEDULE = 4;
    public static final int KEY_DELETE_EXPIRE_SCHEDULE = 5;
    public static final int KEY_MAINTAIN_SCHEDULE = 6;
    public static final int KEY_CREATE_ALTER_DIALOG = 7;
    public static final int KEY_GO_TO_DETAIL_INFO = 8;
    public static final int KEY_UPDATE_IMG = 9;

    private Dialog dialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case KEY_ADD_SCHEDULE:
                    //gotoAddSchedule();
                    addTestMethods();
                    break;
                case KEY_DELETE_SCHEDULE:
                    break;
                case KEY_CREATE_ALTER_DIALOG:
                    createDialog((Integer) msg.obj);
                    break;
                case KEY_GO_TO_DETAIL_INFO:
                    goScheduleDetailActivity((Integer) msg.obj);
                    break;
                case KEY_UPDATE_IMG:
                    if (getCurrentSwitch()) {
                        imgIsGrid.setBackgroundResource(R.drawable.ic_format_list_bulleted_black);
                        setCurrentSwitch(false);
                        updateUI();
                    } else {
                        imgIsGrid.setBackgroundResource(R.drawable.ic_view_module_black);
                        setCurrentSwitch(true);
                        updateUI();
                    }
                    break;
            }
        }
    };

    // 用来做测试的方法
    private void addTestMethods() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_test, null);
        imgVolumeDown = inflate.findViewById(R.id.img_volume_down);
        imgVolumeUp = inflate.findViewById(R.id.img_volume_up);
        imgVolumeDown.setOnClickListener(this);
        imgVolumeUp.setOnClickListener(this);
        dialog.setContentView(inflate);
        dialog.show();
    }

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
        UtilClass.initSystemBar(MainActivity.this);
        gItemDecoration = new GridSpacingItemDecoration(2, 15, true);
        itemDecoration = new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL);
        initUI();

        // 4.4及以上版本开启
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setNavigationBarTintEnabled(true);
//
//        // 自定义颜色
//        tintManager.setTintColor(Color.parseColor("#24b7a4"));
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutIsGrid = getCurrentSwitch();
        updateUI();
    }

    private void initUI() {

        imgTest = (ImageView) this.findViewById(R.id.img_test);
        Bitmap videoThumb = UtilClass.getVideoThumb("/storage/emulated/0/Movies/爱情留在回忆里.mp4", MediaStore.Images.Thumbnails.MINI_KIND);
        int width = videoThumb.getWidth();
        int height = videoThumb.getHeight();
        imgTest.setImageDrawable(new BitmapDrawable(videoThumb));
        Log.e("liu", "width: " + width + " height: " + height);
        btnAddSchedule = (Button) this.findViewById(R.id.btn_add_schedule);
        btnDeleteSchedule = (Button) this.findViewById(R.id.btn_delete_schedule);
        btnModifySchedule = (Button) this.findViewById(R.id.btn_modify_schedule);
        btnQuerySchedule = (Button) this.findViewById(R.id.btn_query_schedule);
        btnDeleteExpireSchedule = (Button) this.findViewById(R.id.btn_delete_expire_schedule);
        btnMaintainSchedule = (Button) this.findViewById(R.id.btn_maintain_schedule);
        radioAddSchedule = (RadioButton) this.findViewById(R.id.radio);

        radioAddSchedule.setOnClickListener(this);
        btnAddSchedule.setOnClickListener(this);
        btnDeleteSchedule.setOnClickListener(this);
        btnModifySchedule.setOnClickListener(this);
        btnQuerySchedule.setOnClickListener(this);
        btnDeleteExpireSchedule.setOnClickListener(this);
        btnMaintainSchedule.setOnClickListener(this);

        imgIsGrid = (ImageView) this.findViewById(R.id.img_grid);
        if (getCurrentSwitch()) {
            imgIsGrid.setBackgroundResource(R.drawable.ic_view_module_black);
        } else {
            imgIsGrid.setBackgroundResource(R.drawable.ic_format_list_bulleted_black);
        }

        imgIsGrid.setOnClickListener(this);

        recyclerViewSchedule = (RecyclerView) this.findViewById(R.id.list_schedule);
        //recyclerViewSchedule.setNestedScrollingEnabled(false); // 使滑动效果是跟随ScrollView去滑动
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
            case R.id.radio:
                message = new Message();
                message.what = KEY_ADD_SCHEDULE;
                handler.sendMessage(message);
                break;
            case R.id.img_grid:
                message = new Message();
                message.what = KEY_UPDATE_IMG;
                handler.sendMessage(message);
                break;
            case R.id.img_volume_down:
                int currentVolume = UtilClass.getCurrentVolume(MainActivity.this);
                if (currentVolume > 0) {
                    UtilClass.setVolumeUp(MainActivity.this, currentVolume - 1);
                }
                break;
            case R.id.img_volume_up:
                int currentVolume1 = UtilClass.getCurrentVolume(MainActivity.this);
                int volumeMax = UtilClass.getVolumeMax(MainActivity.this);
                if (currentVolume1 < volumeMax) {
                    UtilClass.setVolumeUp(MainActivity.this, currentVolume1 + 1);
                }
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
        new AlertDialog.Builder(MainActivity.this).setMessage("是否删除选中数据").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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

    private GridSpacingItemDecoration gItemDecoration;
    private DividerItemDecoration itemDecoration;

    /**
     * 更新界面数据
     */
    public void updateUI() {
        if (getCurrentSwitch()) {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewSchedule.setLayoutManager(manager);
            // 要移除分割线，否则item会不断变大
            recyclerViewSchedule.removeItemDecoration(gItemDecoration);
            recyclerViewSchedule.removeItemDecoration(itemDecoration);
            recyclerViewSchedule.addItemDecoration(itemDecoration);
        } else {
            GridLayoutManager manager = new GridLayoutManager(this, 2);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerViewSchedule.setLayoutManager(manager);
            // 要移除分割线，否则item会不断变大
            recyclerViewSchedule.removeItemDecoration(itemDecoration);
            recyclerViewSchedule.removeItemDecoration(gItemDecoration);
            recyclerViewSchedule.addItemDecoration(gItemDecoration);
        }
        mLists = QueryDb();
        adapter = new ScheduleAdapter(MainActivity.this, mLists, getCurrentSwitch());
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

    public boolean getCurrentSwitch() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        return prefs.getBoolean("grid", false);
    }

    public void setCurrentSwitch(boolean b) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putBoolean("grid", b);
        editor.apply();
    }
}
