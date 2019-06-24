package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.AuthenticationInfo;
import com.qckj.dabei.model.merchant.BusinessShopInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class AuthenticationRequester extends SimpleBaseRequester<AuthenticationInfo> {

    private String userId;

    public AuthenticationRequester(String userId, OnHttpResponseCodeListener<AuthenticationInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected AuthenticationInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toObject(jsonObject.optJSONObject("data"), AuthenticationInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getAuthentication");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
    }
}