package com.example.mytest2019;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Date:2019/5/6
 * Time:14:45
 * author:jiaoyang
 **/
public class BoundSQLite extends SQLiteOpenHelper {
    public static final String create_bound_db = "create table Bound("+
            "diwen integer  NOT NULL,"+
            "gaowen integer  NOT NULL,"+
            "dishi integer  NOT NULL,"+
            "gaoshi integer  NOT NULL)";
    private Context mContext;
    // 第一个参数Context上下文，
    // 第二个参数数据库名，
    // 第三个参数cursor允许我们在查询数据的时候返回一个自定义的光标位置，一般传入的都是null，
    // 第四个参数表示目前库的版本号（用于对库进行升级）
    public  BoundSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory , int version){
        super(context,name ,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_bound_db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新表
        db.execSQL("drop table if exists Bound");
        onCreate(db);
    }
}
