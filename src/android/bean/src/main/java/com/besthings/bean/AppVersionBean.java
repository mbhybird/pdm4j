/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2017-09-11 17:10:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class AppVersionBean {

    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private String SeriesID;
    @JsonProperty("Ret")
    private AppVersionRet Ret;
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

    public void setRet(AppVersionRet Ret) {
        this.Ret = Ret;
    }
    public AppVersionRet getRet() {
        return Ret;
    }

}