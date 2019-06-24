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
 * 重新绑定手机号
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class AgainBindPhoneRequester extends SimpleBaseRequester<List<UserInfo>> {

    private String phone;
    private String code;
    private String userId;

    public AgainBindPhoneRequester(String phone, String code, String userId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.phone = phone;
        this.code = code;
        this.userId = userId;
    }

    @Override
    protected List<UserInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/alterbindingPhone");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("phone", EncryptUtils.encryptData(CommonConfig.aesconfig, phone));
        params.put("code", code);
        params.put("userid", userId);
    }
}
