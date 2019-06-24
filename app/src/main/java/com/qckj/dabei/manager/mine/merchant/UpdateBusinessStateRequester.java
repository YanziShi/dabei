package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import org.json.JSONObject;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/15.
 */
public class UpdateBusinessStateRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private int state;

    public UpdateBusinessStateRequester(String userId, int state, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.state = state;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/updatesjstate");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
        params.put("business_state", state);
    }
}