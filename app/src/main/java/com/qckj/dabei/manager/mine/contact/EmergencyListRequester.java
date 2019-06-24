package com.qckj.dabei.manager.mine.contact;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.contact.ContactInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/6/1.
 */
public class EmergencyListRequester extends SimpleBaseRequester<List<ContactInfo>> {

    private int page;
    private int pageSize;
    private String userId;

    public EmergencyListRequester(int page, int pageSize, String userId, OnHttpResponseCodeListener<List<ContactInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
    }

    @Override
    protected List<ContactInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), ContactInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/getEmergencyLinkmanById");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("user_id", userId);
    }
}
