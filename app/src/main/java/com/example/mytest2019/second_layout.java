package com.example.mytest2019;

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
    //某时间段内的数据存入数组
    String[] date = new String[20];
    int[] Clock = new int[20];
    float[] tempdata = new float[20];
    float[] humidata = new float[20];
    private EditText mEditText1,mEditText2;
    private Button seetime,seetodaytime;
    String tim1,tim2,timToday;
    int index=0;
    private SQLiteHelper dbHelper;  //数据库
    String getmytime;
    int getmyclock;
    float gettemperature,gethumi;
    private ListView lv;

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
                if (mEditText1.length()==0||mEditText2.length()==0)
                    showAlterDialog();  // 警告对话框
                if (mEditText1.length()!=0&&mEditText2.length()!=0){
                    tim1 = mEditText1.getText().toString(); //起始日期
                    tim2 = mEditText2.getText().toString(); //终止日期
                    queryCur(tim1,tim2);
                }
            }
        });
        //查看今日温湿度折线图
        seetodaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //获取今日时间
//                    CurrentTime cur = new CurrentTime();
//                    timToday = cur.getMy_time();
                    timToday = mEditText1.getText().toString();
                    queryToday(timToday);
                    Intent intent = new Intent();
                    Bundle b1 = new Bundle();
                    b1.putStringArray("myhellotimecur",date);
                    intent.putExtras(b1);

                    Bundle b2 = new Bundle();
                    b2.putIntArray("myhelloclockcur",Clock);
                    intent.putExtras(b2);

                    Bundle b3 = new Bundle();
                    b3.putFloatArray("myhellotempcur",tempdata);
                    intent.putExtras(b3);

                    Bundle b4 = new Bundle();
                    b4.putFloatArray("myhellohumicur",humidata);
                    intent.putExtras(b4);

                     Bundle b5 = new Bundle();
                     b5.putBoolean("myhelloisdate",false);
                     intent.putExtras(b5);
                    intent.setClass(second_layout.this,myhellochart.class);
                    startActivity(intent);
            }
        });
    }
    public void FVBI(){
        mEditText1 = findViewById(R.id.datepicker1);
        mEditText2 = findViewById(R.id.datepicker2);
        seetime = findViewById(R.id.button_SeeTime);
        seetodaytime = findViewById(R.id.button_SeeTodayTime);
        lv=findViewById(R.id.listview);
    }
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
                year = year - 2000;
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
    //不能删！！！
    public String trans(String str){
        str = str.replaceAll("\"","\'");
        str = "\"" + str + "\"";
        return str;
    }
    //查询近期温湿度
    public void queryCur(String t1,String t2){
        t1=trans(t1);
        t2=trans(t2);
        Log.d("mycur",t1+" "+t2);
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList <HashMap<String,Object>>();/*在数组中存放数据*/
        dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from timedb WHERE mytime between "+ t1 +" and "+ t2 +"ORDER BY myclock ASC;", null);
        if (cursor != null && cursor.getCount() > 0) {
            index = 0;
            while(cursor.moveToNext()) {
                getmytime = cursor.getString(0);
                getmyclock = cursor.getInt(1);
                gettemperature = cursor.getFloat(2);
                gethumi = cursor.getFloat(3);
                Log.d("mycurdata",
                        index+" "+getmytime+" "+getmyclock+" "+gettemperature+" "+gethumi);
                //存入数组
                date[index]=getmytime;
                Clock[index]=getmyclock;
                tempdata[index]=gettemperature;
                humidata[index++]=gethumi;
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("mytime", getmytime);
                map.put("myclock",getmyclock);
                map.put("temperature", gettemperature);
                map.put("humi",gethumi);
                listItem.add(map);
                SimpleAdapter mSimpleAdapter = new SimpleAdapter(second_layout.this,listItem,R.layout.simple,
                        new String[] {"mytime","myclock","temperature","humi"},
                        new int[] {R.id.ItemText1,R.id.ItemText2,R.id.ItemText3,R.id.ItemText4});
                lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
            }
        }
        cursor.close();
        db.close();
    }
    //查询今日温湿度
    public void queryToday(String t){
        t = trans(t);
        Log.d("mytoday",t);
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList <HashMap<String,Object>>();/*在数组中存放数据*/
        dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from timedb WHERE mytime between "+ t +" and "+ t +"ORDER BY myclock ASC;", null);
        if (cursor != null && cursor.getCount() > 0) {
            index = 0;
            while(cursor.moveToNext()) {
                getmytime = cursor.getString(0);
                getmyclock = cursor.getInt(1);
                gettemperature = cursor.getFloat(2);
                gethumi = cursor.getFloat(3);
                Log.d("mytodaydata",
                        index+" "+getmytime+" "+getmyclock+" "+gettemperature+" "+gethumi);
                //存入数组
                date[index]=getmytime;
                Clock[index]=getmyclock;
                tempdata[index]=gettemperature;
                humidata[index++]=gethumi;
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("mytime", getmytime);
                map.put("myclock",getmyclock);
                map.put("temperature", gettemperature);
                map.put("humi",gethumi);
                listItem.add(map);
                SimpleAdapter mSimpleAdapter = new SimpleAdapter(second_layout.this,listItem,R.layout.simple,
                        new String[] {"mytime","myclock","temperature","humi"},
                        new int[] {R.id.ItemText1,R.id.ItemText2,R.id.ItemText3,R.id.ItemText4});
                lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
            }
        }
        cursor.close();
        db.close();
    }
}