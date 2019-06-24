package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.AuthenticationInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class CreateTopOrderRequester extends SimpleBaseRequester<JSONObject> {

    String userId;
    String zd_id;

    public CreateTopOrderRequester(String userId,String zd_id, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.zd_id = zd_id;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject.optJSONObject("data");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/createZdOrder");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("zd_id", zd_id);
    }
}
