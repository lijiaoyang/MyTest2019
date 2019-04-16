package com.example.mytest2019;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String create_timedb = "create table timedb("+
            "mytime DATE  NOT NULL,"+
            "myclock integer NOT NULL,"+
            "temperature float NOT NULL,"+
            "humi float NOT NULL,"+
            "primary key(mytime,myclock))";

    private Context mContext;
    // 第一个参数Context上下文，
    // 第二个参数数据库名，
    // 第三个参数cursor允许我们在查询数据的时候返回一个自定义的光标位置，一般传入的都是null，
    // 第四个参数表示目前库的版本号（用于对库进行升级）
    public  SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory , int version){
        super(context,name ,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        db.execSQL(create_timedb);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新表
        db.execSQL("drop table if exists usersDB");
        onCreate(db);
    }
}
