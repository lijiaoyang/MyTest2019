package com.example.mytest2019;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.service.autofill.UserData;
import android.util.Log;

public class TestSQLite {
    //一些宏定义和声明
    private static final String TAG = "UserDataManager";
    private static final String DB_NAME = "MyCurrent.db";
    private static final String TABLE_NAME = "Current";   //表名
    private static final int DB_VERSION = 2;
    private Context mContext = null;

    //创建用户book表
    private static final String DB_CREATE = "CREATE TABLE TABLE_NAME ("
            + "TimeId text primary key,"
            + "Temperature integer not null,"
            + "Humidity integer not null"
            +");";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    //DataBaseManagementHelper继承自SQLiteOpenHelper
    private static class DataBaseManagementHelper extends SQLiteOpenHelper {
        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);  //建表语句
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }
    //重写了构造器
    public TestSQLite(Context context) {
        mContext = context;
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mDatabaseHelper.onCreate(mSQLiteDatabase);
        Log.i(TAG, "db has opened!");
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
        Log.i(TAG, "db has closed!");
    }
   //添加新数据
    public long insertBounData(Boundary boundary){
        int temphh = boundary.getTemp_h();
        int templl = boundary.getTemp_l();
        int humihh = boundary.getHumi_h();
        int humill = boundary.getHumi_l();
        ContentValues values = new ContentValues();
        values.put("tempH",temphh);
        values.put("tempL",templl);
        values.put("humiH",humihh);
        values.put("humiL",humill);
        return mSQLiteDatabase.insert(TABLE_NAME, "whatever", values);
    }

    //current表存储近期的温湿度数据。


}



