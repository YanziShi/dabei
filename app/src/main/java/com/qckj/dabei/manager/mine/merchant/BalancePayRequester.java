package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.AuthenticationInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/21.
 */
public class BalancePayRequester extends SimpleBaseRequester<JSONObject> {

    String odrder;
    int type;

    public BalancePayRequester(int type,String odrder, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.odrder = odrder;
        this.type = type;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/joinPartnerYeAppPay");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("odrder", odrder);
        params.put("type", type);
    }
}
