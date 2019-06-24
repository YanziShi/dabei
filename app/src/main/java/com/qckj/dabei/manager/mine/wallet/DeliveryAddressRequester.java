package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.DeliveryAddressInfo;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class DeliveryAddressRequester extends SimpleBaseRequester<List<DeliveryAddressInfo>> {
    private String userId;

    public DeliveryAddressRequester(String userId, OnHttpResponseCodeListener<List<DeliveryAddressInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected List<DeliveryAddressInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), DeliveryAddressInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/getUserAddressById");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
    }
}
