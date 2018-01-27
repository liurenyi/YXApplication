package com.example.schedulemanager;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.database.ScheduleType;
import com.example.schedulemanager.util.UtilClass;

import java.lang.reflect.Field;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText edtTheme;
    public EditText edtRemarks;
    public Spinner spinner;
    public ImageView imgOK;
    public ImageView imgCancle;
    public Button btnAlert;
    public String themeString;
    public String remarksString;
    public String typeString;
    public Schedule mShedule;
    public ScheduleType mScheduleType;
    public Dialog mDialog;
    public View view;
    public DatePicker datePicker;
    public TextView tvSetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        initUI();
    }

    private void initUI() {
        edtRemarks = (EditText) this.findViewById(R.id.edt_remark);
        edtTheme = (EditText) this.findViewById(R.id.edt_theme);
        edtTheme.addTextChangedListener(new ThemeTextWatcher());
        edtRemarks.addTextChangedListener(new RemarksTextWatcher());
        imgOK = (ImageView) this.findViewById(R.id.img_save);
        imgCancle = (ImageView) this.findViewById(R.id.img_back);
        imgOK.setOnClickListener(this);
        imgCancle.setOnClickListener(this);
        spinner = (Spinner) this.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] array = getResources().getStringArray(R.array.spinner_type);
                typeString = array[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnAlert = (Button) this.findViewById(R.id.btn_alert);
        btnAlert.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_save:
                saveAddSchedule();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_alert:
                setAlarmAlert();
                break;
        }
    }

    /**
     * 设置提醒时间设置提醒时间
     */
    private void setAlarmAlert() {
        mDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(this).inflate(R.layout.layout_alarm_alert, null);
        datePicker = view.findViewById(R.id.dateAndTimePicker_datePicker);
        tvSetTime = view.findViewById(R.id.tv_current_set_time);
        tvSetTime.setText(getResources().getString(R.string.tv_set_alarm_time_text, UtilClass.getNowYear(),
                UtilClass.getNowMonth() + 1, UtilClass.getNowDat()));
        tvSetTime.append(UtilClass.getNowDate());
        if (getResources().getConfiguration().locale.getCountry().equals("CN")) {
            // 隐藏掉年份显示，隐藏月份显示只需把最后一个getChildAt值设置为1即可
            ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
        }
        //setDatePickerDividerColor(datePicker);
        datePicker.init(UtilClass.getNowYear(), UtilClass.getNowMonth(), UtilClass.getNowDat(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                tvSetTime.setText(getResources().getString(R.string.tv_set_alarm_time_text, i, i1 + 1, i2));
                tvSetTime.append(UtilClass.getAppointedDate(i, i1, i2));
            }
        });
        mDialog.setContentView(view);
        Window window = mDialog.getWindow();
        WindowManager manager = mDialog.getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
        }
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = display.getWidth() - 40;   // 弹出框宽度与屏幕宽度一致
        window.setAttributes(layoutParams);

        window.setBackgroundDrawableResource(R.drawable.layout_dialog_pop_background); // dialog的背景
        mDialog.show();
    }

    /**
     * 保存新建立的日程
     */
    private void saveAddSchedule() {
        mShedule = new Schedule();
        mShedule.setTitle(themeString);
        mShedule.setNote(remarksString);
        mShedule.setType(typeString);
        mShedule.setDate(UtilClass.getCurrentDate());
        mShedule.setTime(UtilClass.getCurrentTime());
        if (themeString == null) {
            Toast.makeText(this, "必须输入主题", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mShedule.save();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * 监听主题内容的变化
     */
    private class ThemeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            themeString = editable.toString();
        }
    }

    private class RemarksTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            remarksString = editable.toString();
        }
    }
}
