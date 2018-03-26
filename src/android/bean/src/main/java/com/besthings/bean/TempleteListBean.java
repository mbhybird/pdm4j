package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Chas on 2017/10/20 0020.
 */

public class TempleteListBean {

    @JsonProperty("Res")
    private int res;
    @JsonProperty("Desp")
    private String desp;
    @JsonProperty("SeriesID")
    private String seriesid;
    @JsonProperty("Ret")
    private List<TempleteListRet> ret;
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

    public void setRet(List<TempleteListRet> ret) {
        this.ret = ret;
    }
    public List<TempleteListRet> getRet() {
        return ret;
    }

}
