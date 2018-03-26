package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/13 0013.
 */

public class UserListRet {
    @JsonProperty("Code")
    private String Code;
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("Dept")
    private String Dept;
    @JsonProperty("SerialNo")
    private String SerialNo;
    public void setCode(String Code) {
        this.Code = Code;
    }
    public String getCode() {
        return Code;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public String getName() {
        return Name;
    }

    public void setDept(String Dept) {
        this.Dept = Dept;
    }
    public String getDept() {
        return Dept;
    }

    public void setSerialNo(String SerialNo) {
        this.SerialNo = SerialNo;
    }
    public String getSerialNo() {
        return SerialNo;
    }

}