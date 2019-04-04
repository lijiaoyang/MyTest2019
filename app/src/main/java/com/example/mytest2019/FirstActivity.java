package com.example.mytest2019;

import android.content.Intent;
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
                Toast.makeText(FirstActivity.this,"已同步时间到下位机",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
