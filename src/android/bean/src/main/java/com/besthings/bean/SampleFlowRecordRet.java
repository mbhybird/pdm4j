package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/16 0016.
 */

public class SampleFlowRecordRet {
    @JsonProperty("MID")
    private String MID;
    @JsonProperty("SerialNo")
    private int SerialNo;
    @JsonProperty("Type")
    private String Type;
    @JsonProperty("Borrower")
    private String Borrower;
    @JsonProperty("BorrowTime")
    private String BorrowTime;
    @JsonProperty("R1")
    private String R1;
    @JsonProperty("R2")
    private String R2;
    @JsonProperty("ModifyDate")
    private String ModifyDate;

    public void setMID(String MID){
        this.MID = MID;
    }
    public String getMID(){
        return this.MID;
    }
    public void setSerialNo(int SerialNo){
        this.SerialNo = SerialNo;
    }
    public int getSerialNo(){
        return this.SerialNo;
    }
    public void setType(String Type){
        this.Type = Type;
    }
    public String getType(){
        return this.Type;
    }
    public void setBorrower(String Borrower){
        this.Borrower = Borrower;
    }
    public String getBorrower(){
        return this.Borrower;
    }
    public void setBorrowTime(String BorrowTime){
        this.BorrowTime = BorrowTime;
    }
    public String getBorrowTime(){
        return this.BorrowTime;
    }
    public void setR1(String R1){
        this.R1 = R1;
    }
    public String getR1(){
        return this.R1;
    }
    public void setR2(String R2){
        this.R2 = R2;
    }
    public String getR2(){
        return this.R2;
    }
    public void setModifyDate(String ModifyDate){
        this.ModifyDate = ModifyDate;
    }
    public String getModifyDate(){
        return this.ModifyDate;
    }
}
