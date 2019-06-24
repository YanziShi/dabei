package com.qckj.dabei.manager.mine.user;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.CommonConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.util.EncryptUtils;

import org.json.JSONObject;

import java.util.Map;

/**
 * 忘记密码
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class ForgetPasswordRequester extends SimpleBaseRequester<String> {
    private String phoneNum;
    private String verCode;
    private String password;
    private String conPassword;

    public ForgetPasswordRequester(String phoneNum, String verCode, String password, String conPassword, OnHttpResponseCodeListener<String> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.phoneNum = phoneNum;
        this.verCode = verCode;
        this.password = password;
        this.conPassword = conPassword;
    }

    @Override
    protected String onDumpData(JSONObject jsonObject) {
        return jsonObject.optString("data");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/resetPassword");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("phone", EncryptUtils.encryptData(CommonConfig.aesconfig, phoneNum));
        params.put("code", verCode);
        params.put("newPassword", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(password)));
        params.put("setPassword", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(conPassword)));
    }
}
