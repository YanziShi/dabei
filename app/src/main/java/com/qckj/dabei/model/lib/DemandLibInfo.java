package com.qckj.dabei.model.lib;

import com.qckj.dabei.util.json.JsonField;

/**
 * {
 * "F_C_ID":"20180911102136Xs5wr1hTdJdTxP57hL",
 * "F_C_IMGS":"",
 * "F_C_MES":"考虑考虑",
 * "F_C_NAME":"高档餐饮",
 * "F_C_TXIMG":"",
 * "F_D_TIME":1536632496000,
 * "F_ISPREPAY":2,
 * "F_TAB_ADDRESSTWO_ID":"0101",
 * "F_TAB_CLASSIFYTWO_ID":"dbs_2018071812015379",
 * "JULI":8.544
 * }
 * <p>
 * 需求库信息
 * Created by yangzhizhong on 2019/4/9.
 */
public class DemandLibInfo {

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_IMGS")
    private String imgUrl;

    @JsonField("F_C_MES")
    private String mes;

    @JsonField("F_C_NAME")
    private String name;

    @JsonField("F_C_TXIMG")
    private String txImg;

    @JsonField("F_D_TIME")
    private long time;

    @JsonField("F_ISPREPAY")
    private String isPay;

    @JsonField("F_TAB_ADDRESSTWO_ID")
    private String twoId;

    @JsonField("F_TAB_CLASSIFYTWO_ID")
    private String twoClassId;

    @JsonField("JULI")
    private String distance;

    @JsonField("F_C_NICHENG")
    private String nicheng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getTxImg() {
        return txImg;
    }

    public void setTxImg(String txImg) {
        this.txImg = txImg;
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

    public String getTwoId() {
        return twoId;
    }

    public void setTwoId(String twoId) {
        this.twoId = twoId;
    }

    public String getTwoClassId() {
        return twoClassId;
    }

    public void setTwoClassId(String twoClassId) {
        this.twoClassId = twoClassId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }
}
