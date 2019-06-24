package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class GetVercodeRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private String sendPhone;

    public GetVercodeRequester(String userId, String sendPhone, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.sendPhone = sendPhone;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/sendYzm");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("send_phone", sendPhone);
    }

}
