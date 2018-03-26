package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/9/11 0011.
 */

public class DesignFileListRet {
    @JsonProperty("MID")
    private String MID;
    @JsonProperty("LID")
    private String LID;
    @JsonProperty("FileExt")
    private String FileExt;
    @JsonProperty("StyleNo")
    private String StyleNo;
    @JsonProperty("StyleName")
    private String StyleName;
    @JsonProperty("Year")
    private int Year;
    @JsonProperty("Season")
    private String Season;
    @JsonProperty("Theme")
    private String Theme;
    @JsonProperty("Band")
    private String Band;
    @JsonProperty("MakerName")
    private String MakerName;
    @JsonProperty("Maker")
    private String Maker;
    @JsonProperty("Style")
    private String Style;
    @JsonProperty("Brand")
    private String Brand;
    @JsonProperty("Sort")
    private String Sort;
    @JsonProperty("IT")
    private String IT;
    @JsonProperty("AM")
    private String AM;
    @JsonProperty("AD")
    private String AD;
    @JsonProperty("ModifyDate")
    private String ModifyDate;
    @JsonProperty("NM")
    private String NM;
    @JsonProperty("NT")
    private String NT;
    @JsonProperty("DM")
    private String DM;
    @JsonProperty("DT")
    private String DT;
    @JsonProperty("i0")
    private int i0;
    @JsonProperty("i1")
    private int i1;
    @JsonProperty("i2")
    private int i2;
    @JsonProperty("i3")
    private int i3;
    @JsonProperty("i4")
    private int i4;
    @JsonProperty("i5")
    private int i5;
    @JsonProperty("CState")
    private int CState;
    @JsonProperty("iMinPicture")
    private String iMinPicture;
    @JsonProperty("pizhu")
    private String pizhu;

    public String getPizhu() {
        return pizhu;
    }

    public void setPizhu(String pizhu) {
        this.pizhu = pizhu;
    }

    private String[] mSpiltStyle;
    private String Category;

    public void setMID(String MID){
        this.MID = MID;
    }
    public String getMID(){
        return this.MID;
    }
    public void setLID(String LID){
        this.LID = LID;
    }
    public String getLID(){
        return this.LID;
    }
    public void setFileExt(String FileExt){
        this.FileExt = FileExt;
    }
    public String getFileExt(){
        return this.FileExt;
    }
    public void setStyleNo(String StyleNo){
        this.StyleNo = StyleNo;
    }
    public String getStyleNo(){
        return this.StyleNo;
    }
    public void setStyleName(String StyleName){
        this.StyleName = StyleName;
    }
    public String getStyleName(){
        return this.StyleName;
    }
    public void setYear(int Year){
        this.Year = Year;
    }
    public int getYear(){
        return this.Year;
    }
    public void setSeason(String Season){
        this.Season = Season;
    }
    public String getSeason(){
        return this.Season;
    }
    public void setTheme(String Theme){
        this.Theme = Theme;
    }
    public String getTheme(){
        return this.Theme;
    }
    public void setBand(String Band){
        this.Band = Band;
    }
    public String getBand(){
        return this.Band;
    }
    public void setMakerName(String MakerName){
        this.MakerName = MakerName;
    }
    public String getMakerName(){
        return this.MakerName;
    }
    public void setMaker(String Maker){
        this.Maker = Maker;
    }
    public String getMaker(){
        return this.Maker;
    }
    public void setStyle(String Style){
        this.Style = Style;
    }
    public String getStyle(){
        styleSpilt(Style);
        return mSpiltStyle[0];
    }
    public void setBrand(String Brand){
        this.Brand = Brand;
    }
    public String getCategory() {
        styleSpilt(Style);
        return mSpiltStyle[1];}
    public String getBrand(){
        return this.Brand;
    }
    public void setSort(String Sort){
        this.Sort = Sort;
    }
    public String getSort(){
        return this.Sort;
    }
    public void setIT(String IT){
        this.IT = IT;
    }
    public String getIT(){
        return this.IT;
    }
    public void setAM(String AM){
        this.AM = AM;
    }
    public String getAM(){
        return this.AM;
    }
    public void setAD(String AD){
        this.AD = AD;
    }
    public String getAD(){
        return this.AD;
    }
    public void setModifyDate(String ModifyDate){
        this.ModifyDate = ModifyDate;
    }
    public String getModifyDate(){
        return this.ModifyDate;
    }
    public void setNM(String NM){
        this.NM = NM;
    }
    public String getNM(){
        return this.NM;
    }
    public void setNT(String NT){
        this.NT = NT;
    }
    public String getNT(){
        return this.NT;
    }
    public void setDM(String DM){
        this.DM = DM;
    }
    public String getDM(){
        return this.DM;
    }
    public void setDT(String DT){
        this.DT = DT;
    }
    public String getDT(){
        return this.DT;
    }
    public void setI0(int i0){
        this.i0 = i0;
    }
    public int getI0(){
        return this.i0;
    }
    public void setI1(int i1){
        this.i1 = i1;
    }
    public int getI1(){
        return this.i1;
    }
    public void setI2(int i2){
        this.i2 = i2;
    }
    public int getI2(){
        return this.i2;
    }
    public void setI3(int i3){
        this.i3 = i3;
    }
    public int getI3(){
        return this.i3;
    }
    public void setI4(int i4){
        this.i4 = i4;
    }
    public int getI4(){
        return this.i4;
    }
    public void setI5(int i5){
        this.i5 = i5;
    }
    public int getI5(){
        return this.i5;
    }
    public void setCState(int CState){
        this.CState = CState;
    }
    public int getCState(){
        return this.CState;
    }
    public void setIMinPicture(String iMinPicture){
        this.iMinPicture = iMinPicture;
    }
    public String getIMinPicture(){
        return this.iMinPicture;
    }

    public String[] styleSpilt(String style) {
        mSpiltStyle = style.split("/");
        return mSpiltStyle;
    }

}
