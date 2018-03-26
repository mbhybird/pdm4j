package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/18 0018.
 */

public class BuyerSampleNotesRet {
    @JsonProperty("Maker")
    private String maker;
    @JsonProperty("MakeDate")
    private String makedate;
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("rDesc")
    private String rdesc;
    @JsonProperty("PicUrl")
    private String picurl;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }

    public void setRdesc(String rdesc) {
        this.rdesc = rdesc;
    }
    public String getRdesc() {
        return rdesc;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }
    public String getPicurl() {
        return picurl;
    }
}
