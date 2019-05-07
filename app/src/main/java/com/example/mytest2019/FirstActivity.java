package com.example.mytest2019;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;

public class FirstActivity extends AppCompatActivity {
    public Handler receiveHandler,handler,swhandler;
    public String receivewendu,receiveshidu;
    public int reRF,reLF,reJSQ;
    public TextView textViewwendu,textViewshidu;
    public Switch rf_switch,lf_switch,jsq_switch;
    //数据库存储
    int time_oclock;
    float temp,humi;
    String dates;
    private SQLiteHelper dbHelper;  //数据库
    public BoundSQLite BoundHelper;  //边界数据库
    private boundData application;
    public byte[] send=new byte[5];

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button buttonCurrent = findViewById(R.id.button_current);   //近期温湿度
        Button buttonSetting = findViewById(R.id.button_setting);   //设置边界
        Button buttonSynchronize = findViewById(R.id.button_synchronize);   //同步时间
        rf_switch = findViewById(R.id.refeng);
        lf_switch = findViewById(R.id.lengfeng);
        jsq_switch = findViewById(R.id.jiashiqi);
//        Button buttonInse = findViewById(R.id.button_inse);//插入数据库
        textViewwendu=findViewById(R.id.Curwendu);
        textViewshidu=findViewById(R.id.Curshidu);
        //为了使得下位机可以发送UDP数据报
        WifiManager manager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiManager.MulticastLock lock= manager.createMulticastLock("test wifi");
        SynSHT();   //包含开关信息了
        initTimePrompt();
        //获取屏幕宽度高度
        getAndroiodScreenProperty();
        //防止锁屏后进程被杀死
        PowerManager.WakeLock wakeLock;
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
        //按钮
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
        //手动同步温湿度数据
        buttonSynchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendSignalData().start();
                showdialog("已同步数据");
            }
        });
//        buttonInse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToDB();
//            }
//        });
        receiveHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                textViewwendu.setText(receivewendu);
                textViewshidu.setText(receiveshidu);
            }
        };
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                textViewwendu.setText(receivewendu);
                textViewshidu.setText(receiveshidu);
            }
        };
    }

    //定义一个发送json请求数据的代码
    public void SynSHT() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket(6000);
                    InetAddress serverAddr = InetAddress.getByName("192.168.1.103");    //下位机IP地址
                    String s = "s";
                    byte sendSHT[] = s.getBytes();
                    DatagramPacket packet = new DatagramPacket(sendSHT,sendSHT.length,serverAddr,6000);
                    socket.send(packet);
                    socket.close();
                    //接收
                    DatagramSocket socket2 = new DatagramSocket(6000);
                    byte[] data2 = new byte[1024];
                    DatagramPacket packet2 = new DatagramPacket(data2,data2.length);
                    socket2.receive(packet2);
                    byte[] buf = packet2.getData();
                    receivewendu = wendu(buf);
                    receiveshidu= shidu(buf);
//                    reRF = refeng(buf);
//                    reLF= lefeng(buf);
//                    reJSQ = jsq(buf);
//                    Log.d("kaiguan",reRF + " "+reLF+" "+reJSQ);
                    Log.d("wenshidu",receivewendu + " "+receiveshidu);
                    Message msg = new Message();
                    handler.sendMessage(msg);
                    socket2.close();
                    handler.postDelayed(this, 3000);//延迟发送消息，实现3秒一次发送数据
                    SynSHT();       //发送请求
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //同步开关   没用
    public void SynSwitch(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket(6000);
                    InetAddress serverAddr = InetAddress.getByName("192.168.1.103");    //下位机IP地址
                    char s = 'a';       //s.getBytes()
                    send[0] = (byte)s;
                    send[1] = (byte)reRF;
                    send[2] = (byte)reLF;
                    send[3] = (byte)reJSQ;
                    Log.d("toxiaweiji",send[1]+" "+send[2]+" "+send[3]);
                    DatagramPacket packet = new DatagramPacket(send,send.length,serverAddr,6000);
                    socket.send(packet);
                    socket.close();
                    swhandler.postDelayed(this, 3000);//延迟发送消息，实现3秒一次发送数据
                    SynSwitch();       //发送请求
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public class SendSignalData extends Thread{
        @Override
        public void run() {
            try {
                    //发送's'
                    DatagramSocket socket = new DatagramSocket(6000);
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
//        if (a[0].equals("s")) {
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
//        } else {
//            Log.d("mya0",a[0]);
//            return ("未收到温度");
//        }

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
    public int refeng(byte[] b){
        String a[] = new String[11];
        for (int i = 0; i < 10; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);  //字节转成int以16进制显示
            //下位机 RTC 中存储时间时，以 16 进制存储，所以发送数据时也按十六进制发送
            a[i] = hex;
        }
        Log.d("rfff",a[7]);
        if (a[7].equals("1"))
            return 1;
        else
            return 0;
    }
    public  int lefeng(byte[] b){
        String a[] = new String[11];
        for (int i = 0; i < 10; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);  //字节转成int以16进制显示
            //下位机 RTC 中存储时间时，以 16 进制存储，所以发送数据时也按十六进制发送
            a[i] = hex;
        }
        Log.d("lfff",a[8]);
        if (a[8].equals("1"))
            return 1;
        else
            return 0;
    }
    public int jsq(byte[] b){
        String a[] = new String[11];
        for (int i = 0; i < 10; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);  //字节转成int以16进制显示
            //下位机 RTC 中存储时间时，以 16 进制存储，所以发送数据时也按十六进制发送
            a[i] = hex;
        }
        Log.d("jsqq",a[9]);
        if (a[9].equals("1"))
            return 1;
        else
            return 0;
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

    //判断整点存数据进入数据库
    private void initTimePrompt() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, timeFilter);
    }
    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar cal = Calendar.getInstance();
            int min = cal.get(Calendar.MINUTE);
            int sec = cal.get(Calendar.SECOND);
            Log.d("timmm",sec+"");
            if (min==0 && sec==0){
                addToDB();
                Toast.makeText(FirstActivity.this,"存入数据库",Toast.LENGTH_SHORT).show();
            }
            else if (sec==0){   //每分钟更新一次开关
                tongbuSwitch();
                Toast.makeText(FirstActivity.this,"同步开关",Toast.LENGTH_SHORT).show();
            }
        }
    };
    //一分钟判断一次开关
    public void tongbuSwitch(){
        application = (boundData)getApplication();
        int diwen = application.getDiwen();
        int gaowen = application.getGaowen();
        int dishi = application.getDishi();
        int gaoshi = application.getGaoshi();
        if (!receivewendu.equals("")){
            receivewendu = remove_unit(receivewendu);
            temp = Float.parseFloat(receivewendu);
            if (temp <= diwen){
                rf_switch.setChecked(true); //热风开启
                reRF = 1;
            }
            else if (temp >= gaowen){
                lf_switch.setChecked(true); //冷风开启
                reLF = 1;
            }
            else if (temp >= diwen && temp <= gaowen)
            {
                rf_switch.setChecked(false);    //热风关闭
                lf_switch.setChecked(false);    //冷风关闭
                reRF = 0;
                reLF = 0;
            }
        }
        if (!receiveshidu.equals("")){
            receiveshidu = remove_unit(receiveshidu);
            humi = Float.parseFloat(receiveshidu);
            if (humi <= dishi){
                jsq_switch.setChecked(true);
                reJSQ = 1;
            }
            else if (humi >= gaoshi){
                jsq_switch.setChecked(false);
                reJSQ = 0;
            }
        }
    }
    //屏幕尺寸判断
    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
    }
//    //查询数据库的边界值
//    //int Diwen,Gaowen,Dishi,Gaoshi;  //边界数据
//    public void queryBoundDB(){
//        BoundHelper = new BoundSQLite(FirstActivity.this,"Bound",null,1);
//        SQLiteDatabase db = BoundHelper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("select * from timedb",null);
//    }

}
