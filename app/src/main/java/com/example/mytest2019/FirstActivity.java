package com.example.mytest2019;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button buttonCurrent = (Button)findViewById(R.id.button_current);   //近期温湿度
        Button buttonSetting = (Button)findViewById(R.id.button_setting);   //设置边界
        Button buttonSynchronize = (Button)findViewById(R.id.button_synchronize);   //同步时间
        buttonCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //页面跳转
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
                CurrentTime currentTime = new CurrentTime();
                currentTime.GetTime();
                Log.d("yeartime", currentTime.my_time_1);
                Log.d("hourtime", currentTime.my_time_2);
                android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(FirstActivity.this).create();//创建对话框
                dialog.setIcon(R.mipmap.ic_launcher);//设置对话框icon
                dialog.setTitle("提示");//设置对话框标题
                dialog.setMessage("已同步时间到下位机");//设置文字显示内容
                dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//关闭对话框
                    }
                });
                dialog.show();//显示对话框
                //Toast.makeText(FirstActivity.this,"已同步时间到下位机",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
