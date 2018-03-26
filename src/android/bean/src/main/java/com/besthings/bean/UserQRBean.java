/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2017-09-07 15:45:40
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserQRBean {

    @JsonProperty("Svr")
    private String Svr;
    @JsonProperty("Port")
    private int Port;
    @JsonProperty("MID")
    private String MID;
    @JsonProperty("ENO")
    private String ENO;
    @JsonProperty("N")
    private String N;
    public void setSvr(String Svr) {
        this.Svr = Svr;
    }
    public String getSvr() {
        return Svr;
    }

    public void setPort(int Port) {
        this.Port = Port;
    }
    public int getPort() {
        return Port;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }
    public String getMID() {
        return MID;
    }

    public void setENO(String ENO) {
        this.ENO = ENO;
    }
    public String getENO() {
        return ENO;
    }

    public void setN(String N) {
        this.N = N;
    }
    public String getN() {
        return N;
    }

}