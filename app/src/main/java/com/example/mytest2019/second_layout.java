package com.example.mytest2019;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

//近期温湿度
public class second_layout extends AppCompatActivity {
    String PickTime;
    String[] date = new String[20];
    int[] Clock = new int[20];
    int[] tempdata = new int[20];
    int[] humidata = new int[20];
    private EditText mEditText1,mEditText2,mEditText3;
    private Button seetime,seetodaytime,delete;
    String tim1,tim2,deletetime;
    int temp1,humi1,index=0,clock;
    private SQLiteHelper dbHelper;  //数据库
    String getmytime;
    int gettemperature,gethumi,getmyclock;
    private ListView lv;
    EditText tempedit,humiedit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_layout);
        FVBI();
        setScore(mEditText1);
        setScore(mEditText2);
        //查看近期温湿度折线图
        seetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempedit = findViewById(R.id.editText2);
                humiedit = findViewById(R.id.editText3);
                if (mEditText1.length()==0||mEditText2.length()==0||tempedit.length()==0||humiedit.length()==0){
                    showAlterDialog();  // 警告对话框
                }
                if (mEditText1.length()!=0&&mEditText2.length()!=0&&tempedit.length()!=0&&humiedit.length()!=0){
                    temp1 = Integer.parseInt(tempedit.getText().toString());
                    humi1 = Integer.parseInt(humiedit.getText().toString());
                    tim1 = mEditText1.getText().toString(); //起始日期
                    tim2 = mEditText2.getText().toString(); //终止日期
                    addinfo();
                    Toast.makeText(second_layout.this,"已经插入",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //查看当天温湿度折线图
        seetodaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tim1 = mEditText1.getText().toString(); //起始日期
                tim2 = mEditText2.getText().toString(); //终止日期
                if (mEditText1.length()==0||mEditText2.length()==0){
                    showAlterDialog();  // 警告对话框
                }
                else {
                    queryinfo(tim1,tim2);
                    Intent intent = new Intent();

                    Bundle b1 = new Bundle();
                    b1.putStringArray("myhellotimecur",date);
                    intent.putExtras(b1);

                    Bundle b2 = new Bundle();
                    b2.putIntArray("myhellotempcur",tempdata);
                    intent.putExtras(b2);

                    Bundle b3 = new Bundle();
                    b3.putIntArray("myhellohumicur",humidata);
                    intent.putExtras(b3);
                    intent.setClass(second_layout.this,myhellochart.class);
                    startActivity(intent);
                }
            }
        });
        //删除按钮：
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除数据库
                deletetime=((EditText)findViewById(R.id.datepicker1)).getText().toString();
                dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("timedb","mytime=?",new String[] {deletetime});
                db.close();
            }
        });
    }
    public void FVBI(){
        mEditText1 = findViewById(R.id.datepicker1);
        mEditText2 = findViewById(R.id.datepicker2);
        seetime = findViewById(R.id.button_SeeTime);
        seetodaytime = findViewById(R.id.button_SeeTodayTime);
        lv=findViewById(R.id.listview);
        delete = findViewById(R.id.button_delete);
    }
    @SuppressLint("ClickableViewAccessibility")
    public void setScore(final EditText medit){
        medit.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(medit);
                    return true;
                }
                return false;
            }
        });
        medit.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg(medit);
                }
            }
        });
    }
    public void showDatePickDlg(final EditText medit) {
        Calendar calendar = Calendar.getInstance();
        calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(second_layout.this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                if(month>9 && dayOfMonth >9)
                    PickTime = year + "-" +month + "-" + dayOfMonth;
                else if (month>9 && dayOfMonth <=9)
                    PickTime = year + "-" +month + "-0" + dayOfMonth;
                else if (month<=9 && dayOfMonth >9)
                    PickTime = year + "-" +"0" + month + "-" + dayOfMonth;
                else if (month<=9 && dayOfMonth <=9)
                    PickTime = year + "-" +"0" + month + "-0" + dayOfMonth;
                medit.setText(PickTime);
                Toast.makeText(second_layout.this, PickTime, Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showAlterDialog(){
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(second_layout.this);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);//图标
        alterDiaglog.setTitle("警告");//文字
        alterDiaglog.setMessage("日期未填写完整");//提示消息
        alterDiaglog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create();
        alterDiaglog.show();
    }
    //往数据库插入数据
    public void addinfo(){
        dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mytime", tim1);
        values.put("temperature",temp1);
        values.put("humi", humi1);
        db.insert("timedb",null,values);
    }
    //不能删！！！
    public String trans(String str){
        str = str.replaceAll("\"","\'");
        str = "\"" + str + "\"";
        return str;
    }
    public void queryinfo(String t1,String t2){
        t1 = trans(t1);
        t2 = trans(t2);
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList <HashMap<String,Object>>();/*在数组中存放数据*/
        dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from timedb WHERE mytime between "+ t1 +" and "+ t2 , null);  //+" ORDER BY mytime ASC"
        if (cursor != null && cursor.getCount() > 0) {
            index = 0;
            while(cursor.moveToNext()) {
                getmytime = cursor.getString(0);
                gettemperature = cursor.getInt(1);
                gethumi = cursor.getInt(2);
                //存入数组
                date[index]=getmytime;
                tempdata[index]=gettemperature;
                humidata[index++]=gethumi;
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("mytime", getmytime);
                map.put("myclock",getmyclock);
                map.put("temperature", gettemperature);
                map.put("humi",gethumi);
                listItem.add(map);
                SimpleAdapter mSimpleAdapter = new SimpleAdapter(second_layout.this,listItem,R.layout.simple,
                        new String[] {"mytime","temperature","humi"},
                        new int[] {R.id.ItemText1,R.id.ItemText2,R.id.ItemText3});
                lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
            }
        }
        cursor.close();
        db.close();
    }
}