package com.example.mytest2019;

/**
 * Date:2019/3/8
 * Time:9:28
 * author:jiaoyang
 **/
public class CurrentData {
    public int TimeId;
    public int Temperature;
    public int Humidity;

    public int getTimeId() {
        return TimeId;
    }
    public int getTemperature() {
        return Temperature;
    }
    public int getHumidity() {
        return Humidity;
    }
    public CurrentData(int TimeId,int Temperature,int Humidity){
        super();
        this.TimeId = TimeId;
        this.Temperature = Temperature;
        this.Humidity = Humidity;
    }
}
