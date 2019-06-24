package com.qckj.dabei.model.merchant;

import com.qckj.dabei.util.json.JsonField;

import java.io.Serializable;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class AuthenticationInfo implements Serializable {
    @JsonField("F_C_ID")
    String merchantID;                  //商家id

    @JsonField("F_C_DPNAME")
    String shopName;              //店铺名称

    @JsonField("F_C_JJPHONE")
    String shopPhone;             //店铺电话

    @JsonField("F_TAB_CLASSIFYTWO_ID")
    String shopTypeId;    //店铺分类id

    @JsonField("classifytwo_name")
    String shopTypeName;        //店铺分类名称

    @JsonField("address")
    String officeAddr;                 //行政地址

    @JsonField("F_C_ADDRESS")
    String address;             //详细地址

    @JsonField("shop")
    String shopImage;                    //店铺封面

    @JsonField("F_C_YYZZIMG")
    String certificateImage;             //营业执照照片

    @JsonField("street_id")
    String streetId;               //街道id

    @JsonField("street_name")
    String streetName;             //街道名称

    @JsonField("business_district_id")
    String busiDistrictId;          //商圈id

    @JsonField("business_district_name")
    String busiDistrictName;         //商圈名称

    @JsonField("F_D_ISQIYE")
    String authType;                  //认证类型

    @JsonField("F_C_FRNAME")
    String peopleName;               //法人姓名

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getShopTypeId() {
        return shopTypeId;
    }

    public void setShopTypeId(String shopTypeId) {
        this.shopTypeId = shopTypeId;
    }

    public String getShopTypeName() {
        return shopTypeName;
    }

    public void setShopTypeName(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }

    public String getOfficeAddr() {
        return officeAddr;
    }

    public void setOfficeAddr(String officeAddr) {
        this.officeAddr = officeAddr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getCertificateImage() {
        return certificateImage;
    }

    public void setCertificateImage(String certificateImage) {
        this.certificateImage = certificateImage;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBusiDistrictId() {
        return busiDistrictId;
    }

    public void setBusiDistrictId(String busiDistrictId) {
        this.busiDistrictId = busiDistrictId;
    }

    public String getBusiDistrictName() {
        return busiDistrictName;
    }

    public void setBusiDistrictName(String busiDistrictName) {
        this.busiDistrictName = busiDistrictName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }
}
