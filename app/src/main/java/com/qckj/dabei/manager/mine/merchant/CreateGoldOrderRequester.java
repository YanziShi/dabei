package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/22.
 */
public class CreateGoldOrderRequester extends SimpleBaseRequester<JSONObject> {

    String userId;
    String jp_id;

    public CreateGoldOrderRequester(String userId,String jp_id, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.jp_id = jp_id;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject.optJSONObject("data");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/createJpOrder");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("jp_id", jp_id);
    }
}