package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/11 0011.
 */

public class DesignFileListPicBean {
    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private String SeriesID;
    @JsonProperty("Ret")
    private String Ret;

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
    public void setRet(String Ret){
        this.Ret = Ret;
    }
    public String getRet(){
        return this.Ret;
    }
}
