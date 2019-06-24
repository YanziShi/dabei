package com.qckj.dabei.manager.mine.msg;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/23.
 */
public class AgentInfoRequester extends SimpleBaseRequester<JSONObject> {
    private String userId;

    public AgentInfoRequester( String userId, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getDlsInfoByUserId");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
    }
}
