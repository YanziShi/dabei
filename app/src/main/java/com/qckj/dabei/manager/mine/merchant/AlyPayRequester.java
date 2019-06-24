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
public class AlyPayRequester extends SimpleBaseRequester<JSONObject> {

    String odrder;
    double total_fee;
    int type;

    public AlyPayRequester(int type,String odrder,double total_fee, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.odrder = odrder;
        this.total_fee = total_fee;
        this.type = type;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/joinPartnerZfbAppPay");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("odrder", odrder);
        params.put("type", type);
        params.put("total_fee", total_fee);
    }
}
