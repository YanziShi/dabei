package com.qckj.dabei.model.merchant;

import com.qckj.dabei.util.json.JsonField;

/**
 * Created by yangzhizhong on 2019/5/21.
 */
public class GoldMerchantsInfo {
    @JsonField("F_C_ID")
    String id;                              //商户id

    @JsonField("gold_merchants_cost")
    double cost;                 //金牌费用

    @JsonField("gold_merchants_hint")
    String hint;                 //提示语

    @JsonField("gold_merchants_introduction")
    String intro;         //介绍

    @JsonField("gold_merchants_name")
    String name;                 //名称

    @JsonField("gold_merchants_unit")
    String unit;                 //时间单位

    @JsonField("gold_merchants_url")
    String url;                  //金牌图片url

    @JsonField("gold_merchants_zkcost")
    String discount;               //折扣后的费用

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
