package com.qckj.dabei.manager.mine.user;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.CommonConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.EncryptUtils;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 验证码登录
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class LoginVerificationCodeRequester extends SimpleBaseRequester<List<UserInfo>> {

    private String phoneNumber;
    private String verCode;

    public LoginVerificationCodeRequester(String phoneNumber, String verCode, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.phoneNumber = phoneNumber;
        this.verCode = verCode;
    }

    @Override
    protected List<UserInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/userLoginByPhone");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("phoneInput", EncryptUtils.encryptData(CommonConfig.aesconfig, phoneNumber));
        params.put("code", verCode);
    }
}
