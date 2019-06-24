package com.qckj.dabei.manager.mine.user;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * 获取手机验证码
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class GetVerificationCodeRequester extends SimpleBaseRequester<String> {

    private String phoneNumber;

    public GetVerificationCodeRequester(String phoneNumber, OnHttpResponseCodeListener<String> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected String onDumpData(JSONObject jsonObject) {
        return jsonObject.optString("data");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getPhoneCode");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("phone", phoneNumber);
    }
}
