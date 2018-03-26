package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by chas on 2017/11/2.
 */

public class AnalysisOfResearchStatisticalSummaryBean {

    @JsonProperty("Res")
    private int res;
    @JsonProperty("Desp")
    private String desp;
    @JsonProperty("SeriesID")
    private String seriesid;
    @JsonProperty("Ret")
    private List<AnalysisOfResearchStatisticalSummaryRet> ret;
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

    public void setRet(List<AnalysisOfResearchStatisticalSummaryRet> ret) {
        this.ret = ret;
    }
    public List<AnalysisOfResearchStatisticalSummaryRet> getRet() {
        return ret;
    }
}
