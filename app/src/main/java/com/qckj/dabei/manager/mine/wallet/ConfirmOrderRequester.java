package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserInfo;

import org.json.JSONObject;

import java.util.Map;

/**
 * 确认收货(我的收益-兑换记录)
 */
public class ConfirmOrderRequester extends SimpleBaseRequester<JSONObject> {
    private String orderId;
    String userId;

    public ConfirmOrderRequester(String userId,String orderId, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.orderId = orderId;
        this.userId = userId;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/store/confirmOrder");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("order_id", orderId);

    }
}
