package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/23 0023.
 */

public class ResponseBean {
    @JsonProperty("Res")
    private int res;
    @JsonProperty("Desp")
    private String desp;
    @JsonProperty("SeriesID")
    private String seriesid;
    @JsonProperty("Ret")
    private String ret;
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

    public void setRet(String ret) {
        this.ret = ret;
    }
    public String getRet() {
        return ret;
    }
}
