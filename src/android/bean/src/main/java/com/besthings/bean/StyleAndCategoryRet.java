package com.besthings.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Chas on 2017/10/20 0020.
 */

public class StyleAndCategoryRet {
    @JsonProperty("StyleA")
    private String stylea;
    @JsonProperty("StyleB")
    private String styleb;
    public void setStylea(String stylea) {
        this.stylea = stylea;
    }
    public String getStylea() {
        return stylea;
    }

    public void setStyleb(String styleb) {
        this.styleb = styleb;
    }
    public String getStyleb() {
        return styleb;
    }

}
