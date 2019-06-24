package com.qckj.dabei.model.merchant;

import java.io.Serializable;

/**
 * Created by yangzhizhong on 2019/5/17.
 */
public class MyServiceInfo implements Serializable {
    String imgs;            //服务详情图片说明
    double money;           //服务价格
    String goodsid;         //服务Id
    String serviceUnit;     //服务单位
    String state;           //商品状态0=未发布;1=已发布;2=商品下架,-1=商品删除
    String serviceCover;    //服务封面
    String serviceName;     //服务名称
    String category;        //服务类别
    String message;         //服务介绍
    String classifytwoId;   //类别Id

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getServiceCover() {
        return serviceCover;
    }

    public void setServiceCover(String serviceCover) {
        this.serviceCover = serviceCover;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClassifytwoId() {
        return classifytwoId;
    }

    public void setClassifytwoId(String classifytwoId) {
        this.classifytwoId = classifytwoId;
    }
}
