package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Chas on 2017/11/4 0004.
 */

public class PersonCustomBean {

    @JsonProperty("Res")
    private int res;
    @JsonProperty("Desp")
    private String desp;
    @JsonProperty("SeriesID")
    private String seriesid;
    @JsonProperty("Ret")
    private List<PersonCustomRet> ret;
    public void setRes(int res) {
        this.res = res;
    }
    public int getRes() {
        return res;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }
    public String getDesp() {
        return desp;
    }

    public void setSeriesid(String seriesid) {
        this.seriesid = seriesid;
    }
    public String getSeriesid() {
        return seriesid;
    }

    public void setRet(List<PersonCustomRet> ret) {
        this.ret = ret;
    }
    public List<PersonCustomRet> getRet() {
        return ret;
    }
}
