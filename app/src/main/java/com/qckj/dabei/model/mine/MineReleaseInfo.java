package com.qckj.dabei.model.mine;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * {
 * "F_C_ID":"2018071915173605qIsUx6XTeKesynzU",
 * "F_C_MES":"我能提供各种包裹寄件，收件，上门服务",
 * "F_C_NAME":"快递",
 * "F_C_PHONE":"",
 * "F_D_TIME":"2018-07-19 15:17:36",
 * "F_ISPREPAY":1,
 * "F_I_MONEY":10,
 * "F_I_STA":1,
 * "count":2
 * }
 * <p>
 * 我的发布信息
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineReleaseInfo implements Serializable {
    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_MES")
    private String mes;

    @JsonField("F_C_NAME")
    private String name;

    @JsonField("F_C_PHONE")
    private String phone;

    @JsonField("F_D_TIME")
    private long time;
    //是否预付：1=预付；2=不预付
    @JsonField("F_ISPREPAY")
    private String isPay;

    @JsonField("F_I_MONEY")
    private String money;
    // 状态:0：等待，1接单中，2完成，3不显示,4交易失败
    @JsonField("F_I_STA")
    private String state;

    @JsonField("count")
    private String count;

    @JsonField("image")
    private String image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
