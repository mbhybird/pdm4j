package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Chas on 2017/10/13 0013.
 */

public class UserListBean {
    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private String SeriesID;
    @JsonProperty("Ret")
    private List<UserListRet> Ret;
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

    public void setRet(List<UserListRet> Ret) {
        this.Ret = Ret;
    }
    public List<UserListRet> getRet() {
        return Ret;
    }

}