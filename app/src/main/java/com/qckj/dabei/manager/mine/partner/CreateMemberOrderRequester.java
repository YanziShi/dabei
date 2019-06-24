package com.qckj.dabei.manager.mine.partner;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/30.
 */
public class CreateMemberOrderRequester extends SimpleBaseRequester<JSONObject> {

    String userId;
    String member_grade;

    public CreateMemberOrderRequester(String userId,String member_grade, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.member_grade = member_grade;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject.optJSONObject("data");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/createMemberOrder");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("member_grade", member_grade);
    }
}
