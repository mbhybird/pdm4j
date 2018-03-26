package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/9/28 0028.
 */

public class DesignMaterialRet {


    @JsonProperty("MID")
    private String mid;
    @JsonProperty("MaterialNo")
    private String materialno;
    @JsonProperty("MaterialName")
    private String materialname;
    @JsonProperty("Color")
    private String color;
    @JsonProperty("ColorNo")
    private String colorno;
    @JsonProperty("Width")
    private String width;
    @JsonProperty("Unit")
    private String unit;
    @JsonProperty("SupplierName")
    private String suppliername;
    @JsonProperty("Num")
    private int num;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Risk")
    private String risk;
    @JsonProperty("Specs")
    private String specs;
    @JsonProperty("SupplierColorNo")
    private String suppliercolorno;
    @JsonProperty("Season")
    private String season;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Price")
    private String price;
    @JsonProperty("MoneyUnit")
    private String moneyunit;
    @JsonProperty("LinkMan")
    private String linkman;
    @JsonProperty("SupplierPhone")
    private String supplierphone;
    @JsonProperty("SupplierAddr")
    private String supplieraddr;
    @JsonProperty("pic")
    private String pic;
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getMid() {
        return mid;
    }

    public void setMaterialno(String materialno) {
        this.materialno = materialno;
    }
    public String getMaterialno() {
        return materialno;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }
    public String getMaterialname() {
        return materialname;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getColor() {
        return color;
    }

    public void setColorno(String colorno) {
        this.colorno = colorno;
    }
    public String getColorno() {
        return colorno;
    }

    public void setWidth(String width) {
        this.width = width;
    }
    public String getWidth() {
        return width;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return unit;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }
    public String getSuppliername() {
        return suppliername;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public int getNum() {
        return num;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
    public String getRisk() {
        return risk;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
    public String getSpecs() {
        return specs;
    }

    public void setSuppliercolorno(String suppliercolorno) {
        this.suppliercolorno = suppliercolorno;
    }
    public String getSuppliercolorno() {
        return suppliercolorno;
    }

    public void setSeason(String season) {
        this.season = season;
    }
    public String getSeason() {
        return season;
    }

    public void setYear(String year) {
        this.year = year;
    }
    public String getYear() {
        return year;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }

    public void setMoneyunit(String moneyunit) {
        this.moneyunit = moneyunit;
    }
    public String getMoneyunit() {
        return moneyunit;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }
    public String getLinkman() {
        return linkman;
    }

    public void setSupplierphone(String supplierphone) {
        this.supplierphone = supplierphone;
    }
    public String getSupplierphone() {
        return supplierphone;
    }

    public void setSupplieraddr(String supplieraddr) {
        this.supplieraddr = supplieraddr;
    }
    public String getSupplieraddr() {
        return supplieraddr;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getPic() {
        return pic;
    }
}
