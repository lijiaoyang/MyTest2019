package com.example.mytest2019;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    private BoundSQLite BoundHelper;  //边界数据库
    private boundData application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        FVID();
        init();
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSetValue();
                String tell = "温度范围："+diwen+"-"+gaowen+"湿度范围："+dishi+"-"+gaoshi;
                Toast.makeText(Setting.this, tell,Toast.LENGTH_LONG).show();
                application = (boundData)getApplication();
                application.setData(diwen,gaowen,dishi,gaoshi);
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
        mTempL.setMinValue(15);
        mTempL.setValue(20);
        //高温
        mTempH.setMaxValue(27);
        mTempH.setMinValue(23);
        mTempH.setValue(25);
        //低湿度
        mHumiL.setMaxValue(55);
        mHumiL.setMinValue(45);
        mHumiL.setValue(50);
        //高湿度
        mHumiH.setMaxValue(80);
        mHumiH.setMinValue(56);
        mHumiH.setValue(70);
    }
    public void getSetValue() {
        gaowen = mTempH.getValue();
        diwen = mTempL.getValue();
        gaoshi = mHumiH.getValue();
        dishi = mHumiL.getValue();
    }
    public void BoundToDB(){
        BoundHelper = new BoundSQLite(Setting.this,"Bound",null,1);
        SQLiteDatabase db = BoundHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diwen", diwen);
        values.put("gaowen",gaowen);
        values.put("dishi",dishi);
        values.put("gaoshi", gaoshi);
        db.insert("Bound",null,values);
        Toast.makeText(Setting.this,"已上传数据库",Toast.LENGTH_SHORT).show();
    }
}
