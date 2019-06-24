package com.qckj.dabei.manager.mine.auth;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/14.
 */
public class MerchantCertificationRequester extends SimpleBaseRequester<JSONObject> {

    String authType;     //Enterprise_individual;认证类别
    String tradeType;    //calsstwoid;行业类别
    String address;      //alladdresses;地址省+市+区
    String phone;        //服务电话
    String shopName;     //商店名、企业名
    String picCover;   //shop;封面
    String userid;       //用户id
    double userLat;       //用户所在经度
    double userLon;       //用户所在的纬度
    String name;         //企业代表
    String detailAddr;   //defultaddresses;详细地址
    String streetId;     //street_id街道id
    String busiCircleId; // businessDistrict_id;
    String picEnterprise;//shop_pic营业执照

    public MerchantCertificationRequester(String authType, String tradeType, String address, String phone, String shopName,
                                          String picCover, String userid, double userLat, double userLon, String name, String detailAddr,
                                          String streetId, String busiCircleId, String picEnterprise, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.authType = authType;
        this.tradeType = tradeType;
        this.address = address;
        this.phone = phone;
        this.shopName = shopName;
        this.picCover = picCover;
        this.userid = userid;
        this.userLat = userLat;
        this.userLon = userLon;
        this.name = name;
        this.detailAddr = detailAddr;
        this.streetId = streetId;
        this.busiCircleId = busiCircleId;
        this.picEnterprise = picEnterprise;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/addMerchantCertification");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("Enterprise_individual", authType);
        params.put("calsstwoid", tradeType);
        params.put("alladdresses", address);
        params.put("phone", phone);
        params.put("shopName", shopName);
        params.put("shop", picCover);
        params.put("userid", userid);
        params.put("user_y", userLat);
        params.put("user_x", userLon);
        params.put("name", name);
        params.put("defultaddresses", detailAddr);
        params.put("street_id", streetId);
        params.put("businessDistrict_id", busiCircleId);
        params.put("shop_pic", picEnterprise);
    }
}