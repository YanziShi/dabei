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
 * 绑定手机
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class BindPhoneRequester extends SimpleBaseRequester<List<UserInfo>> {

    private String phoneNumber;
    private String code;
    private String openId;

    public BindPhoneRequester(String phoneNumber, String code, String openId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.openId = openId;
    }

    @Override
    protected List<UserInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/bindingPhone");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("phone", EncryptUtils.encryptData(CommonConfig.aesconfig, phoneNumber));
        params.put("code", code);
        params.put("openid", EncryptUtils.encryptData(CommonConfig.aesconfig, openId));
    }
}
