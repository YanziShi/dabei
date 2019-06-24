package com.qckj.dabei.model.merchant;

import com.qckj.dabei.util.json.JsonField;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class ShopTopInfo {
    @JsonField("F_C_ID")
    String topId;

    @JsonField("adcode")
    String adcode;

    @JsonField("zd_cost")
    double topPrice;

    @JsonField("zd_name")
    String topTimeName;

    public String getTopId() {
        return topId;
    }

    public void setTopId(String topId) {
        this.topId = topId;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public double getTopPrice() {
        return topPrice;
    }

    public void setTopPrice(double topPrice) {
        this.topPrice = topPrice;
    }

    public String getTopTimeName() {
        return topTimeName;
    }

    public void setTopTimeName(String topTimeName) {
        this.topTimeName = topTimeName;
    }
}
