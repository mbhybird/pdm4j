package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by Chas on 2017/11/6 0006.
 */

public class OrderNumChartRet {

    @JsonProperty("sType")
    private int stype;
    @JsonProperty("Day")
    private String day;
    @JsonProperty("OrderNum")
    private int ordernum;
    public void setStype(int stype) {
        this.stype = stype;
    }
    public int getStype() {
        return stype;
    }

    public void setDay(String day) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }

    public void setOrdernum(int ordernum) {
        this.ordernum = ordernum;
    }
    public int getOrdernum() {
        return ordernum;
    }
}
