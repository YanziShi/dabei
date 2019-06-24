package com.qckj.dabei.manager.mine.contact;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * 添加紧急联系人
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class AddEmergencyContactRequest extends SimpleBaseRequester<String> {

    private String userId;
    private String contactName;
    private String contactPhone;

    public AddEmergencyContactRequest(String userId, String contactName, String contactPhone, OnHttpResponseCodeListener<String> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    @Override
    protected String onDumpData(JSONObject jsonObject) {
        return jsonObject.optString("success");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/addEmergencyLinkman");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("emergency_linkman", contactName);
        params.put("emergency_linkman_phone", contactPhone);
    }
}
