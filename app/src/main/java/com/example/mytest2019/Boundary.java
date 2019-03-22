package com.example.mytest2019;

/**
 * Date:2019/3/1
 * Time:1:37
 * author:jiaoyang
 **/
public class Boundary {
    private int Temp_h;
    private int Temp_l;
    private int Humi_h;
    private int Humi_l;

    public int getHumi_h() {
        return Humi_h;
    }

    public int getHumi_l() {
        return Humi_l;
    }

    public int getTemp_h() {
        return Temp_h;
    }

    public int getTemp_l() {
        return Temp_l;
    }

    public Boundary(int Temp_h, int Temp_l, int Humi_h, int Humi_l){
        super();
        this.Temp_h = Temp_h;
        this.Temp_l = Temp_l;
        this.Humi_h = Humi_h;
        this.Humi_l = Humi_l;
    }
}
