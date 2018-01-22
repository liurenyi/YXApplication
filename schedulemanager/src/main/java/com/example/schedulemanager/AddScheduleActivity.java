package com.example.schedulemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.database.ScheduleType;
import com.example.schedulemanager.util.UtilClass;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText edtTheme;
    public EditText edtRemarks;
    public Button btnOK;
    public Button btnCancle;
    public String themeString;
    public String remarksString;
    private Schedule mShedule;
    private ScheduleType mScheduleType;


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

        btnOK = (Button) this.findViewById(R.id.btn_ok);
        btnCancle = (Button) this.findViewById(R.id.btn_cancle);
        btnOK.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                saveAddSchedule();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_cancle:
                finish();
                break;
        }
    }

    /**
     * 保存新建立的日程
     */
    private void saveAddSchedule() {
        mShedule = new Schedule();
        mShedule.setTitle(themeString);
        mShedule.setNote(remarksString);
        mShedule.setDate(UtilClass.getCurrentDate());
        mShedule.setTime(UtilClass.getCurrentTime());
        mShedule.save();
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
