package com.example.mytest2019;

import android.app.Application;

/**
 * Date:2019/5/6
 * Time:15:15
 * author:jiaoyang
 **/
public class boundData extends Application {
    public int diwen,gaowen,dishi,gaoshi;

    public int getDiwen() {
        return diwen;
    }
    public int getGaowen() {
        return gaowen;
    }
    public int getDishi() {
        return dishi;
    }
    public int getGaoshi() {
        return gaoshi;
    }
    public void setData(int diwen,int gaowen,int dishi,int gaoshi){
        this.diwen = diwen;
        this.gaowen = gaowen;
        this.dishi = dishi;
        this.gaoshi = gaoshi;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setData(20,25,55,70); //初始化全局变量
    }
}
