package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Created by chas on 2017/11/3.
 */

public class AnalysisOfResearchChartRet {

    @JsonProperty("SNO")
    private int sno;
    @JsonProperty("s")
    private List<Integer> s;
    public void setSno(int sno) {
        this.sno = sno;
    }
    public int getSno() {
        return sno;
    }

    public void setS(List<Integer> s) {
        this.s = s;
    }
    public List<Integer> getS() {
        return s;
    }
}
