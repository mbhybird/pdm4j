package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by chas on 2017/10/26.
 */

public class SpecialTechnicsDetailBean {

    @JsonProperty("Res")
    private int res;
    @JsonProperty("Desp")
    private String desp;
    @JsonProperty("SeriesID")
    private String seriesid;
    @JsonProperty("Ret")
    private List<SpecialTechnicsDetailRet> ret;
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

    public void setRet(List<SpecialTechnicsDetailRet> ret) {
        this.ret = ret;
    }
    public List<SpecialTechnicsDetailRet> getRet() {
        return ret;
    }
}
