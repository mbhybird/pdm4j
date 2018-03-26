package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/9/11 0011.
 */

public class GraphicNotesRet {
    @JsonProperty("MID")
    private String MID;
    @JsonProperty("Maker")
    private String Maker;
    @JsonProperty("MakeDate")
    private String MakeDate;
    @JsonProperty("Desc")
    private String Desc;

    public void setMID(String MID){
        this.MID = MID;
    }
    public String getMID(){
        return this.MID;
    }
    public void setMaker(String Maker){
        this.Maker = Maker;
    }
    public String getMaker(){
        return this.Maker;
    }
    public void setMakeDate(String MakeDate){
        this.MakeDate = MakeDate;
    }
    public String getMakeDate(){
        return this.MakeDate;
    }
    public void setDesc(String Desc){
        this.Desc = Desc;
    }
    public String getDesc(){
        return this.Desc;
    }
}
