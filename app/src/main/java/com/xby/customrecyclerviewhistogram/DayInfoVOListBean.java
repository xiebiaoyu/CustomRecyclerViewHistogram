package com.xby.customrecyclerviewhistogram;

/**
 * Created by Administrator on 2019/3/19 0019.
 */

public class DayInfoVOListBean {
    private int dayTake;
    private String dayIncome;
    private long dayTime;

    public DayInfoVOListBean(int dayTake, String dayIncome, long dayTime) {
        this.dayTake = dayTake;
        this.dayIncome = dayIncome;
        this.dayTime = dayTime;
    }

    public int getDayTake() {
        return dayTake;
    }

    public void setDayTake(int dayTake) {
        this.dayTake = dayTake;
    }

    public String getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(String dayIncome) {
        this.dayIncome = dayIncome;
    }

    public long getDayTime() {
        return dayTime;
    }

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }
}
