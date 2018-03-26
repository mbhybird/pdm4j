/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auto-generated: 2017-09-24 20:16:57
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BaseBean {
    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private String SeriesID;
    @JsonProperty("Ret")
    private List<BaseRet> Ret;
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

    public void setRet(List<BaseRet> Ret) {
        this.Ret = Ret;
    }
    public List<BaseRet> getRet() {
        return Ret;
    }

}