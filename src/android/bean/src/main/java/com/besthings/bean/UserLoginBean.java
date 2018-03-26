/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2017-09-07 14:8:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserLoginBean {

    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private String SeriesID;
    @JsonProperty("Ret")
    private UserLoginRet Ret;
    public void setRes(int Res) {
        this.Res = Res;
    }
    public int getRes() {
        return Res;
    }

    public void setDesp(String Desp) {
        this.Desp = Desp;
    }
    public String getDesp() {
        return Desp;
    }

    public void setSeriesID(String SeriesID) {
        this.SeriesID = SeriesID;
    }
    public String getSeriesID() {
        return SeriesID;
    }

    public void setRet(UserLoginRet Ret) {
        this.Ret = Ret;
    }
    public UserLoginRet getRet() {
        return Ret;
    }

}