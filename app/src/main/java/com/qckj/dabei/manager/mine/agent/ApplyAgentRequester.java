package com.qckj.dabei.manager.mine.agent;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/30.
 */
public class ApplyAgentRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private String name;
    private String phone;
    private String city;
    public ApplyAgentRequester(String userId, String name,String phone,String city, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.city = city;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/addAgentCertification");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
        params.put("name", name);
        params.put("alladdresses", city);
        params.put("phone", phone);

    }
}
