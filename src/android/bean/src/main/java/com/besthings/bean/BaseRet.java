/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2017-09-24 20:16:57
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BaseRet {
    @JsonProperty("SerialNo")
    private int SerialNo;
    @JsonProperty("TypeNo")
    private String TypeNo;
    @JsonProperty("TypeName")
    private String TypeName;
    public void setSerialNo(int SerialNo) {
        this.SerialNo = SerialNo;
    }
    public int getSerialNo() {
        return SerialNo;
    }

    public void setTypeNo(String TypeNo) {
        this.TypeNo = TypeNo;
    }
    public String getTypeNo() {
        return TypeNo;
    }

    public void setTypeName(String TypeName) {
        this.TypeName = TypeName;
    }
    public String getTypeName() {
        return TypeName;
    }

}