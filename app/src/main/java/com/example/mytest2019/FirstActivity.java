package com.example.mytest2019;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button buttonCurrent = (Button)findViewById(R.id.button_current);
        Button buttonSetting = (Button)findViewById(R.id.button_setting);
        buttonCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this,"近期的温湿度",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FirstActivity.this,second_layout.class);
                startActivity(intent);
            }
        });
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this,"设置边界",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FirstActivity.this,Setting.class);
                startActivity(intent);
            }
        });
    }
}
