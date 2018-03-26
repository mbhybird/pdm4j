package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/23 0023.
 */

public class SampleManagerEditionRecordRet {

    @JsonProperty("MID")
    private String mid;
    @JsonProperty("LID")
    private String lid;
    @JsonProperty("cDesc")
    private String cdesc;
    @JsonProperty("Difference")
    private String difference;
    @JsonProperty("Sort")
    private String sort;
    @JsonProperty("Finish")
    private String finish;
    @JsonProperty("Maker")
    private String maker;
    @JsonProperty("MakeDate")
    private String makedate;
    private String pic;
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getMid() {
        return mid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
    public String getLid() {
        return lid;
    }

    public void setCdesc(String cdesc) {
        this.cdesc = cdesc;
    }
    public String getCdesc() {
        return cdesc;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }
    public String getDifference() {
        return difference;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getSort() {
        return sort;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
    public String getFinish() {
        return finish;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }
    public String getMaker() {
        return maker;
    }

    public void setMakedate(String makedate) {
        this.makedate = makedate;
    }
    public String getMakedate() {
        return makedate;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getPic() {
        return pic;
    }

}
