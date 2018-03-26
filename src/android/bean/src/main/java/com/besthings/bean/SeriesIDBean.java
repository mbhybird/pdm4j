/**
  * Copyright 2017 bejson.com 
  */
package com.besthings.bean;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Auto-generated: 2017-08-30 11:12:14
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class SeriesIDBean {

    @JsonProperty("Res")
    private int Res;
    @JsonProperty("Desp")
    private String Desp;
    @JsonProperty("SeriesID")
    private int SeriesID;
    @JsonProperty("Ret")
    private List<SeriesIDRet> Ret;
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

    public void setSeriesID(int SeriesID) {
         this.SeriesID = SeriesID;
     }
     public int getSeriesID() {
         return SeriesID;
     }

    public void setRet(List<SeriesIDRet> Ret) {
         this.Ret = Ret;
     }
     public List<SeriesIDRet> getRet() {
         return Ret;
     }

}