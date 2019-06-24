package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/7.
 */
public class GoodsServiceRequester extends SimpleBaseRequester<JSONObject> {

    private String userid;
    private String xuqiuId;

    public GoodsServiceRequester(String userid, String xuqiuId, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userid = userid;
        this.xuqiuId = xuqiuId;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/goodsService");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userid);
        params.put("xuqiuId", xuqiuId);
    }
}
