package com.qckj.dabei.manager;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.StartAdvertiseInfo;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/31.
 */
public class StartAdvertiseRequester extends SimpleBaseRequester<StartAdvertiseInfo> {

    private String city;
    private String district;

    public StartAdvertiseRequester(String city, String district, OnHttpResponseCodeListener <StartAdvertiseInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.city = city;
        this.district = district;
    }

    @Override
    protected StartAdvertiseInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toObject(jsonObject.optJSONObject("data"), StartAdvertiseInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/bootPageAds");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("addressname_two", city);
        params.put("addressname_three", district);
    }
}
