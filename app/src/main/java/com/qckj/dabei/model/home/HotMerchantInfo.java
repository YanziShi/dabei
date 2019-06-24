package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * {
 * "F_C_ADDRESS": "玉厂路284号家园内",
 * "F_C_DPNAME": "紫薇汽车用品商行",
 * "F_C_FMIMG": "http://dabei.oss-cn-shenzhen.aliyuncs.com/upload/dianpu/20180718160736aCNHNmQDFyO7Si52Sq.jpg",
 * "F_C_ID": "20180718141159YtQFOEDXAlXzdqZ1Yk",
 * "F_C_JJPHONE": "18985191628",
 * "F_C_NAME": "洗车场",
 * "F_C_X": "106.705254",
 * "F_C_Y": "26.5598",
 * "JULI": "11,677.29",
 * "URL": "http://www.dabeiinfo.com/"
 * }
 * <p>
 * 热门商家
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HotMerchantInfo implements Serializable {

    @JsonField("F_C_ID")
    private String id;

    @JsonField("F_C_DPNAME")
    private String dpName;

    @JsonField("F_C_NAME")
    private String name;

    @JsonField("F_C_JJPHONE")
    private String phone;

    @JsonField("F_C_X")
    private String longitude;

    @JsonField("F_C_Y")
    private String latitude;

    @JsonField("JULI")
    private String distance;

    @JsonField("F_C_ADDRESS")
    private String address;

    @JsonField("F_C_FMIMG")
    private String imgUrl;

    @JsonField("URL")
    private String url;

    @JsonField("isjpsj")
    private int isGold;

    @JsonField("isdpzd")
    private int isTop;

    @JsonField("business_state")
    private int isRest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsGold() {
        return isGold;
    }

    public void setIsGold(int isGold) {
        this.isGold = isGold;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsRest() {
        return isRest;
    }

    public void setIsRest(int isRest) {
        this.isRest = isRest;
    }

}
