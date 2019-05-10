package com.example.mytest2019;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
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


public class myhellochart extends AppCompatActivity {
    private LineChartView lineChart,lineChart2;
    String[] date = new String[20];
    int[] clockdata = new int[20];
    float[] tempdata = new float[20];
    float[] humidata = new float[20];
    Boolean isdate;
    private List<PointValue> mPointTempValues = new ArrayList<PointValue>();    //温度
    private List<PointValue> mPointHumiValues = new ArrayList<PointValue>();   //湿度
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhellochart_layout);
        Bundle b1 = this.getIntent().getExtras();
        date = b1.getStringArray("myhellotimecur");
        Bundle b2 = this.getIntent().getExtras();
        clockdata = b2.getIntArray("myhelloclockcur");
        Bundle b3 = this.getIntent().getExtras();
        tempdata = b3.getFloatArray("myhellotempcur");
        Bundle b4 = this.getIntent().getExtras();
        humidata = b4.getFloatArray("myhellohumicur");
        Bundle b5 = this.getIntent().getExtras();
        isdate = b5.getBoolean("myhelloisdate");
        for (int i = 0; i < date.length; i++) {
            Log.d("myhello123",date[i]+" "+clockdata[i]+" "+tempdata[i]+" "+humidata[i]+" "+isdate);
        }
        lineChart = findViewById(R.id.line_chart);
        lineChart2 = findViewById(R.id.line_chart2);
        getAxisXLables(isdate);
        //温度数据
        getAxisTempPoints();
        //湿度数据
        getAxisHumiPoints();
        initTempLineChart();
        initHumiLineChart();
    }

    //设置X 轴的显示
    private void getAxisXLables(Boolean isDate) {
        if (isDate){
            //x轴 为 日期
            for (int i = 0; date[i]!=null; i++) {
                mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
            }
        }
        else {
            //x轴 为 时钟
            for (int i = 0; clockdata[i]!=0; i++) {
                mAxisXValues.add(new AxisValue(i).setLabel(clockdata[i]+""));
            }
        }
    }
    //温度数据的显示---y轴
    private void getAxisTempPoints() {
        for (int i = 0; tempdata[i]!=0; i++) {
            mPointTempValues.add(new PointValue(i, tempdata[i]));
        }
    }
    //湿度数据的显示---y轴
    private void getAxisHumiPoints() {
        for (int i = 0; humidata[i]!=0; i++) {
            mPointHumiValues.add(new PointValue(i, humidata[i]));
        }
    }
    private void initTempLineChart() {
        Line line = new Line(mPointTempValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setFormatter(new SimpleLineChartValueFormatter(1));
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
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setMaxLabelChars(7);//最多几个Y轴坐标
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
    private void initHumiLineChart(){
        Line line1 = new Line(mPointHumiValues).setColor(Color.parseColor("#7AC5CD")); // 折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line1.setFormatter(new SimpleLineChartValueFormatter(1));
        line1.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line1.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line1.setFilled(false);//是否填充曲线的面积
        line1.setHasLabels(true);//曲线的数据坐标是否加上备注
        line1.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line1.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line1);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setMaxLabelChars(7);//最多几个Y轴坐标
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart2.setInteractive(true);
        lineChart2.setZoomEnabled(true); //可以放大
        lineChart2.setZoomType(ZoomType.HORIZONTAL);
        lineChart2.setMaxZoom((float) 2);//最大方法比例
        lineChart2.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);  //横向滚动
        lineChart2.setLineChartData(data);   //把数据放进chart里
        lineChart2.setVisibility(View.VISIBLE);
        //X轴数据的显示个数
        Viewport v = new Viewport(lineChart2.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart2.setCurrentViewport(v);
    }
}
