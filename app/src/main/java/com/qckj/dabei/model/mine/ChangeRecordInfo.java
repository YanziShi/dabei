package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

/**
 * Created by yangzhizhong on 2019/5/24.
 */
public class ChangeRecordInfo {
    @JsonField("F_C_ID")
    String id;

    @JsonField("goods_name")
    String name;

    @JsonField("goods_number")
    int goodsNumber;

    @JsonField("order_state")
    int orderState;

    @JsonField("goods_original_bbprice")
    String beizhuPrice;

    @JsonField("goods_original_rmbprice")
    String rmbPrice;

    @JsonField("goods_sales_bbprice")
    String saleBeizhuPrice;

    @JsonField("goods_sales_rmbprice")
    String saleRmbPrice;

    @JsonField("goods_photo_url")
    String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getBeizhuPrice() {
        return beizhuPrice;
    }

    public void setBeizhuPrice(String beizhuPrice) {
        this.beizhuPrice = beizhuPrice;
    }

    public String getRmbPrice() {
        return rmbPrice;
    }

    public void setRmbPrice(String rmbPrice) {
        this.rmbPrice = rmbPrice;
    }

    public String getSaleBeizhuPrice() {
        return saleBeizhuPrice;
    }

    public void setSaleBeizhuPrice(String saleBeizhuPrice) {
        this.saleBeizhuPrice = saleBeizhuPrice;
    }

    public String getSaleRmbPrice() {
        return saleRmbPrice;
    }

    public void setSaleRmbPrice(String saleRmbPrice) {
        this.saleRmbPrice = saleRmbPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
