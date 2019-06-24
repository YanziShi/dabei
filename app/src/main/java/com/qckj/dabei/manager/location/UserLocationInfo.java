package com.qckj.dabei.manager.location;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * 用户位置信息
 * <p>
 * Created by yangzhizhong
 */

public class UserLocationInfo implements Serializable {
    @JsonField("province")
    private String province = "";

    @JsonField("city")
    private String city = "";

    @JsonField("district")
    private String district = ""; // 区

    @JsonField("adCode")
    private String adCode; // 区域编码

    @JsonField("latitude")
    private double latitude; // 纬度

    @JsonField("longitude")
    private double longitude; // 经度

    @NonNull
    @Override
    public String toString() {
        return "UserLocationInfo{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", adCode='" + adCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
