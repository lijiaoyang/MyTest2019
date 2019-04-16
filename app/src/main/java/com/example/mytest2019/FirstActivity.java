package com.example.mytest2019;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Calendar;
import java.util.TimeZone;

public class FirstActivity extends AppCompatActivity {

    private DatagramSocket socket;
    public Handler receiveHandler;
    public String receivewendu,receiveshidu;
    public TextView textViewwendu,textViewshidu;
    //数据库存储
    int time_oclock;
    float temp,humi;
    String dates;
    private SQLiteHelper dbHelper;  //数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button buttonCurrent = findViewById(R.id.button_current);   //近期温湿度
        Button buttonSetting = findViewById(R.id.button_setting);   //设置边界
        Button buttonSynchronize = findViewById(R.id.button_synchronize);   //同步时间
        Button buttonInse = findViewById(R.id.button_inse);//插入数据库
        textViewwendu=findViewById(R.id.Curwendu);
        textViewshidu=findViewById(R.id.Curshidu);
        //为了使得下位机可以发送UDP数据报
        WifiManager manager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiManager.MulticastLock lock= manager.createMulticastLock("test wifi");
        buttonCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,second_layout.class);
                startActivity(intent);
            }
        });
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this,Setting.class);
                startActivity(intent);
            }
        });
        buttonSynchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendSignalData().start();
                showdialog("已同步数据");
            }
        });
        buttonInse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDB();
            }
        });
        receiveHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                textViewwendu.setText(receivewendu);
                textViewshidu.setText(receiveshidu);
                socket.close();
            }
        };
    }
    public class SendSignalData extends Thread{
        @Override
        public void run() {
            try {
                    //发送's'
                    socket = new DatagramSocket(6000);
                    InetAddress serverAddr = InetAddress.getByName("192.168.1.103");    //下位机IP地址
                    String s = "s";
                    byte send[] = s.getBytes();
                    DatagramPacket packet = new DatagramPacket(send,send.length,serverAddr,6000);
                    socket.send(packet);
                    socket.close();
                    Log.d("mysignal", " ");
                    //接收
                    DatagramSocket socket2 = new DatagramSocket(6000);
                    byte[] data2 = new byte[1024];
                    DatagramPacket packet2 = new DatagramPacket(data2,data2.length);
                    socket2.receive(packet2);
                    byte[] buf = packet2.getData();
                    receivewendu = wendu(buf);
                    receiveshidu= shidu(buf);
                    Log.d("wenshidu",receivewendu + " "+receiveshidu);
                    Message msg = new Message();
                    receiveHandler.sendMessage(msg);
                    socket2.close();
            }catch (Exception e)
            {
                // TODO Auto-generated catch block
            }
        }
    }
    public String wendu(byte[] b) {
        String ret = "";
        String a[] = new String[10];
        for (int i = 0; i < 4; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);  //字节转成int以16进制显示
            //下位机 RTC 中存储时间时，以 16 进制存储，所以发送数据时也按十六进制发送
            if (hex.startsWith("0"))    //如果以0开始
                hex = hex.substring(1);     //除了首部0，全部赋给hex
            a[i] = hex;
        }
//        if (a[0].equals('s')) {
            if (a[3].length() == 0)    //小数为0
            {
                if (a[2].length() == 0)    //个位为0
                    return a[1] + "0" + "." + "0" + "℃";
                else
                    return a[1] + a[2] + "." + "0" + "℃";
            } else if (a[3].length() != 0) {
                if (a[2].length() == 0)    //个位为0
                    return a[1] + "0" + "." + a[3] + "℃";
                else
                    return a[1] + a[2] + "." + a[3] + "℃";
            }else
                return ret;
//        } else return ("未收到温度");
    }
    public String shidu(byte[] b){
        String ret = "";
        String a[]=new String[10];
        for (int i = 0; i < 7; i++)
        {
            String hex = Integer.toHexString(b[ i ] & 0xFF);
            if (hex.startsWith("0"))
                hex = hex.substring(1);
            a[i]=hex;
        }
//        if(a[0].equals("s"))
//        {
            if(a[6].length()==0)    //小数为0
            {
                if(a[5].length()==0)    //个位为0
                    return a[4]+"0"+"."+"0"+"%";
                else
                    return a[4]+a[5]+"."+"0"+"%";
            }
            else if(a[6].length()!=0)    //小数不为0
            {
                if(a[5].length()==0)    //个位为0
                    return a[4]+"0"+"."+a[6]+"%";
                else
                    return a[4]+a[5]+"."+a[6]+"%";
            } else
                return ret;
//        }
//        else return("未收到湿度");
    }
    private void showdialog(String s){
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(FirstActivity.this).create();//创建对话框
        dialog.setIcon(R.mipmap.ic_launcher);//设置对话框icon
        dialog.setTitle("提示");//设置对话框标题
        dialog.setMessage(s);//设置文字显示内容
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭对话框
            }
        });
        dialog.show();//显示对话框
    }
    //往数据库插入数据
    public void addToDB(){
        dbHelper = new SQLiteHelper(FirstActivity.this,"timedb",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //切记！！
        CurrentTime mycurtime = new CurrentTime();
        dates = mycurtime.getMy_time();
        time_oclock = mycurtime.getHour();
        if (!receivewendu.equals("")){
            receivewendu = remove_unit(receivewendu);
            temp = Float.parseFloat(receivewendu);
        }
        if (!receiveshidu.equals("")){
            receiveshidu = remove_unit(receiveshidu);
            humi = Float.parseFloat(receiveshidu);
        }
        Log.d("myinse",dates+" "+time_oclock+" "+temp+" "+humi);
        values.put("mytime", dates);
        values.put("myclock",time_oclock);
        values.put("temperature",temp);
        values.put("humi", humi);
        db.insert("timedb",null,values);
        Toast.makeText(FirstActivity.this,"已上传数据库",Toast.LENGTH_SHORT).show();
    }
    //去除日期单位
    public String remove_unit(String s){
        String newdates;
        newdates = s.substring(0,4);
        return newdates;
    }
}
