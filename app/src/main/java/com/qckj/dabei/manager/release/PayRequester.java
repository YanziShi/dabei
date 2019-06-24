package com.qckj.dabei.manager.release;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * 支付请求
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class PayRequester extends SimpleBaseRequester<String> {

    private String totalFee;
    private String name;
    private String orderId;
    private int payType;
    private String userId;
    private String ip;

    public PayRequester(String totalFee, String name, String orderId, int payType,
                        String userId, String ip, OnHttpResponseCodeListener<String> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.totalFee = totalFee;
        this.name = name;
        this.orderId = orderId;
        this.payType = payType;
        this.userId = userId;
        this.ip = ip;
    }

    @Override
    protected String onDumpData(JSONObject jsonObject) {
        if (payType == 1) {
            return jsonObject.optJSONObject("data").toString();
        } else if (payType == 2) {
            return jsonObject.optString("orderStr");
        } else {
            return "";
        }
    }

    @NonNull
    @Override
    public String getServerUrl() {
        if (payType == 1) {
            return SystemConfig.getServerUrl("/wxpay");
        } else if (payType == 2) {
            return SystemConfig.getServerUrl("/aliPay");
        } else {
            return "";
        }
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("total_fee", totalFee);
        params.put("body", name);
        params.put("OrderId", orderId);
        params.put("zhifufs", payType);
        params.put("userid", userId);
        params.put("spbill_create_ip", ip);
    }
}
