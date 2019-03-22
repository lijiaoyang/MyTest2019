package com.example.mytest2019;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class Setting extends AppCompatActivity {
    NumberPicker mTempH,mTempL,mHumiH,mHumiL;
    Button mSetting;
    int diwen,gaowen,dishi,gaoshi;
    public TestSQLite mTestSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        FVID();
        init();
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //InputToDatabase();
                getSetValue();
                String tell = "温度范围："+diwen+"-"+gaowen+"湿度范围："+dishi+"-"+gaoshi;
                Toast.makeText(Setting.this, tell,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void FVID(){
        mTempH = findViewById(R.id.picker_tempH);
        mTempL = findViewById(R.id.picker_tempL);
        mHumiH = findViewById(R.id.picker_humiH);
        mHumiL = findViewById(R.id.picker_humiL);
        mSetting = findViewById(R.id.button_input);
    }
    private void init(){
        //低温
        mTempL.setMaxValue(22);
        mTempL.setMinValue(18);
        mTempL.setValue(20);
        //高温
        mTempH.setMaxValue(27);
        mTempH.setMinValue(23);
        mTempH.setValue(25);
        //低湿度
        mHumiL.setMaxValue(29);
        mHumiL.setMinValue(20);
        mHumiL.setValue(25);
        //高湿度
        mHumiH.setMaxValue(40);
        mHumiH.setMinValue(30);
        mHumiH.setValue(35);
    }
    public void getSetValue() {
        gaowen = mTempH.getValue();
        diwen = mTempL.getValue();
        gaoshi = mHumiH.getValue();
        dishi = mHumiL.getValue();
    }
    private void listener(){
        mTempH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
             @Override
             public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                 String toast = "oldVal：" + oldVal + "   newVal：" + newVal;
                 Toast.makeText(Setting.this, toast, Toast.LENGTH_SHORT).show();
             }
         });
        mTempL.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String toast = "oldVal：" + oldVal + "   newVal：" + newVal;
                Toast.makeText(Setting.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
        mHumiH.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String toast = "oldVal：" + oldVal + "   newVal：" + newVal;
                Toast.makeText(Setting.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
        mHumiL.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String toast = "oldVal：" + oldVal + "   newVal：" + newVal;
                Toast.makeText(Setting.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public void InputToDatabase(){
//        int TempH = Integer.parseInt(mTempH.getEditableText().toString().trim());
//        int TempL = Integer.parseInt(mTempL.getEditableText().toString().trim());
//        int HumiH = Integer.parseInt(mHumiH.getEditableText().toString().trim());
//        int HumiL = Integer.parseInt(mHumiL.getEditableText().toString().trim());
//        if (mTestSQLite==null){
//            mTestSQLite = new TestSQLite(this);
//        }
//        mTestSQLite.openDataBase();
//        Boundary mBoundary =new Boundary(TempH,TempL,HumiH,HumiL);
//        long flag = mTestSQLite.insertBounData(mBoundary);
//        if (flag!=-1) {
//            Log.i("success666","");
//        }
//        mTestSQLite.closeDataBase();
//
//
//
//    }

}
