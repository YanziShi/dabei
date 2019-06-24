package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/21.
 */
public class WeixinPayRequester extends SimpleBaseRequester<JSONObject> {

    int type;
    String odrder;
    double total_fee;

    public WeixinPayRequester(int type,String odrder,double total_fee, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.type = type;
        this.odrder = odrder;
        this.total_fee = total_fee;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/joinPartnerWxAppPay");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("odrder", odrder);
        params.put("type", type);
        params.put("total_fee", total_fee);
    }
}