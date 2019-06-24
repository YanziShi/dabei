package com.qckj.dabei.model.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.util.json.JsonField;

/**
 * "F_ADDRESSONE":"贵州省",
 * "F_ADDRESSTHREE":"息烽",
 * "F_ADDRESSTWO":"贵阳市",
 * "F_PARTNER_BEIZU":"家政服务",
 * "F_PARTNER_ID":"db_201810241736jksfsf",
 * "F_PARTNER_NAME":"阿姨帮",
 * "F_PARTNER_URL":"https://h5.ayibang.com/#!/index",
 * "F_deafuladdress":"扎佐镇襄阳路",
 * "partner_img":"https://dabei.oss-cn-shenzhen.aliyuncs.com/all/%E9%98%BF%E5%A7%A8%E5%B8%AE%402x.png",
 * "sortid":5
 * <p>
 * 品牌合作商信息
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBrandPartnerInfo {

    @JsonField("F_ADDRESSONE")
    private String province;

    @JsonField("F_ADDRESSTHREE")
    private String county;

    @JsonField("F_ADDRESSTWO")
    private String city;

    @JsonField("F_PARTNER_BEIZU")
    private String detail;

    @JsonField("F_PARTNER_ID")
    private String id;

    @JsonField("F_PARTNER_NAME")
    private String name;

    @JsonField("F_PARTNER_URL")
    private String url;

    @JsonField("partner_img")
    private String imgUrl;

    @JsonField("sortid")
    private String sortId;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeBrandPartnerInfo{" +
                "province='" + province + '\'' +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                ", detail='" + detail + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", sortId='" + sortId + '\'' +
                '}';
    }
}
