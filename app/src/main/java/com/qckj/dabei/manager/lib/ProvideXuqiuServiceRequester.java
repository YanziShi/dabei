package com.qckj.dabei.manager.lib;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.lib.DemandLibInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/23.
 */
public class ProvideXuqiuServiceRequester extends SimpleBaseRequester<JSONObject> {

    private String id;
    private String user_id;

    public ProvideXuqiuServiceRequester( String user_id, String id, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.id = id;
        this.user_id = user_id;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/provideXuqiuService");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("xuqiuId", id);
        params.put("userid", user_id);
    }
}
