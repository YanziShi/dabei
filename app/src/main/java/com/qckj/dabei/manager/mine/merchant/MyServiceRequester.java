package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.MerchantInfo;
import com.qckj.dabei.model.merchant.MyServiceInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/17.
 */
public class MyServiceRequester extends SimpleBaseRequester<List<MyServiceInfo>> {

    private String userId;

    public MyServiceRequester(String userId, OnHttpResponseCodeListener<List<MyServiceInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected List<MyServiceInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), MyServiceInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/selectGoods");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
    }
}

