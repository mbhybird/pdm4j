/**
 * Copyright 2017 bejson.com
 */
package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Auto-generated: 2017-09-07 14:8:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserLoginRet {

    @JsonProperty("Maker")
    private String Maker;
    @JsonProperty("UserName")
    private String UserName;
    @JsonProperty("Active")
    private int Active;
    @JsonProperty("MobilePhone")
    private String MobilePhone;
    public void setMaker(String Maker) {
        this.Maker = Maker;
    }
    public String getMaker() {
        return Maker;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    public String getUserName() {
        return UserName;
    }

    public void setActive(int Active) {
        this.Active = Active;
    }
    public int getActive() {
        return Active;
    }

    public void setMobilePhone(String MobilePhone) {
        this.MobilePhone = MobilePhone;
    }
    public String getMobilePhone() {
        return MobilePhone;
    }

}