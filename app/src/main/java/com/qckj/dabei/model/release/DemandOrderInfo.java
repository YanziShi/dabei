package com.qckj.dabei.model.release;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * {
 * "OrderId":"12bcdcc471ab4381bba51fc27ec6454f",
 * "xqid":"dff707ee47d74c8ebc7d10a7dadc442d"
 * classifytwoName : 高档餐饮
 * money : 13.0
 * }
 * <p>
 * 发布的信息
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class DemandOrderInfo implements Serializable {

    @JsonField("OrderId")
    private String orderId;

    @JsonField("xqid")
    private String xqId;

    @JsonField("classifytwoName")
    private String name;

    @JsonField("money")
    private double money;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getXqId() {
        return xqId;
    }

    public void setXqId(String xqId) {
        this.xqId = xqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
