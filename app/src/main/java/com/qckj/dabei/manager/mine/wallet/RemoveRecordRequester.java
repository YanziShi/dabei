package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.WalletInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/24.
 */
public class RemoveRecordRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private String orderId;

    public RemoveRecordRequester(String userId, String orderId,OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.orderId = orderId;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/store/removeorderById");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("order_id", orderId);

    }
}
