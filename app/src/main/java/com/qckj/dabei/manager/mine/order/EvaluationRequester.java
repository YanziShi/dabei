package com.qckj.dabei.manager.mine.order;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/6/2.
 */
public class EvaluationRequester extends SimpleBaseRequester<JSONObject> {

    private String userid;
    private String userOrderid;
    private String message;
    private int number;

    public EvaluationRequester( String userid,String userOrderid, String message,int number, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userOrderid = userOrderid;
        this.userid = userid;
        this.message = message;
        this.number = number;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/evaluateService");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userOrderid", userOrderid);
        params.put("userid", userid);
        params.put("message", message);
        params.put("number", number);
    }
}
