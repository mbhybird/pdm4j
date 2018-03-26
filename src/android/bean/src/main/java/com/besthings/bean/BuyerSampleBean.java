/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auto-generated: 2017-09-27 9:44:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BuyerSampleBean {
    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private String SeriesID;
    @JsonProperty("Ret")
    private List<BuyerSampleRet> Ret ;

    public void setRes(int Res){
        this.Res = Res;
    }
    public int getRes(){
        return this.Res;
    }
    public void setDesp(String Desp){
        this.Desp = Desp;
    }
    public String getDesp(){
        return this.Desp;
    }
    public void setSeriesID(String SeriesID){
        this.SeriesID = SeriesID;
    }
    public String getSeriesID(){
        return this.SeriesID;
    }
    public void setRet(List<BuyerSampleRet> Ret){
        this.Ret = Ret;
    }
    public List<BuyerSampleRet> getRet(){
        return this.Ret;
    }
}