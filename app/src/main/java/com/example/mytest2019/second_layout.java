package com.example.mytest2019;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Picture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
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
import android.database.sqlite.SQLiteOpenHelper;
import org.w3c.dom.Text;

//近期温湿度
public class second_layout extends AppCompatActivity {
    private LineChartView lineChart;
    String PickTime;
    String[] date = new String[15];
    int[] score= new int[15];
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private EditText mEditText1,mEditText2;
    private Button seetime,seetodaytime,seetemp,seehumi;
    String tim1,tim2;
    int temp1,humi1,index=0;
    private SQLiteHelper dbHelper;  //数据库
    String getmytime;
    int gettemperature,gethumi;
    private ListView lv;
    EditText tempedit,humiedit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_layout);
        FVBI();
        setScore(mEditText1);
        setScore(mEditText2);
        seetemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart(1);//初始化
            }
        });
        seehumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart(2);//初始化
            }
        });
        seetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempedit = (EditText)findViewById(R.id.editText2);
                humiedit = (EditText)findViewById(R.id.editText3);
                tim1 = mEditText1.getText().toString();
                tim2 = mEditText2.getText().toString();
                temp1 = Integer.parseInt(tempedit.getText().toString());
                humi1 = Integer.parseInt(humiedit.getText().toString());
                addinfo();
                Toast.makeText(second_layout.this,"已经插入",Toast.LENGTH_SHORT).show();
            }
        });
        seetodaytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryinfo();
                for (int i = 0; i < date.length; i++) {
                    Log.d("mydate",date[i]+" "+score[i]);
                }
                Toast.makeText(second_layout.this,"查询数据库",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void FVBI(){
        mEditText1 = (EditText) findViewById(R.id.datepicker1);
        mEditText2 = (EditText) findViewById(R.id.datepicker2);
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        seetime = (Button)findViewById(R.id.button_SeeTime);
        seetodaytime = (Button)findViewById(R.id.button_SeeTodayTime);
        lv=(ListView)findViewById(R.id.listview);
        seetemp = (Button)findViewById(R.id.button_SeeTemp);
        seehumi = (Button)findViewById(R.id.button_SeeHumi);
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
                if(month>9 && dayOfMonth >9)
                    PickTime = month + "-" + dayOfMonth;
                else if (month>9 && dayOfMonth <=9)
                    PickTime = month + "-0" + dayOfMonth;
                else if (month<=9 && dayOfMonth >9)
                    PickTime = "0" + month + "-" + dayOfMonth;
                else if (month<=9 && dayOfMonth <=9)
                    PickTime = "0" + month + "-0" + dayOfMonth;
                medit.setText(PickTime);
                Toast.makeText(second_layout.this, PickTime, Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }
    private void initLineChart(int type) {
        mAxisXValues.clear();
        mPointValues.clear();
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        if(type==1){
            axisY.setName("温度");
        }else if(type==2){
            axisY.setName("湿度");
        }
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomEnabled(true); //可以放大
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);  //横向滚动
        lineChart.setLineChartData(data);   //把数据放进chart里
        lineChart.setVisibility(View.VISIBLE);
        //X轴数据的显示个数
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    public void addinfo(){
        dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mytime", tim1);
        values.put("temperature",temp1);
        values.put("humi", humi1);
        db.insert("timedb",null,values);
    }
    public void queryinfo(){
        final ArrayList<HashMap<String, Object>> listItem = new ArrayList <HashMap<String,Object>>();/*在数组中存放数据*/
        //第二个参数是数据库名
        dbHelper = new SQLiteHelper(second_layout.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from timedb", null);
        if (cursor != null && cursor.getCount() > 0) {
            index = 0;
            while(cursor.moveToNext()) {
                getmytime = cursor.getString(0);
                gettemperature = cursor.getInt(1);
                gethumi = cursor.getInt(2);
                Log.d("bishe",getmytime);
                Log.d("myindex",index+"");
                date[index]=getmytime;
                score[index++]=gettemperature;

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("mytime", getmytime);
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