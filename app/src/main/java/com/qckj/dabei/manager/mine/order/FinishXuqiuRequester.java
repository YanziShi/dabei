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
public class FinishXuqiuRequester extends SimpleBaseRequester<JSONObject> {

    private String xuqiuid;
    private String userid;
    private String userOrderid;

    public FinishXuqiuRequester( String userid,String xuqiuid, String userOrderid, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.xuqiuid = xuqiuid;
        this.userid = userid;
        this.userOrderid = userOrderid;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/romoveXuqiu");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("xuqiuid", xuqiuid);
        params.put("userid", userid);
        params.put("userOrderid", userOrderid);
    }
}
