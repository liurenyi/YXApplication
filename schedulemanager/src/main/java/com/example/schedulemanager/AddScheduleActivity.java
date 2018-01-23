package com.example.schedulemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.schedulemanager.database.Schedule;
import com.example.schedulemanager.database.ScheduleType;
import com.example.schedulemanager.util.UtilClass;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText edtTheme;
    public EditText edtRemarks;
    public Spinner spinner;
    public ImageView imgOK;
    public ImageView imgCancle;

    public String themeString;
    public String remarksString;
    public String typeString;

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
        }
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
