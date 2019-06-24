package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HomeBannerInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取首页banner信息
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class GetHomeBannerInfoRequester extends SimpleBaseRequester<List<HomeBannerInfo>> {

    private String city;
    private String district;

    public GetHomeBannerInfoRequester(String city, String district, OnHttpResponseCodeListener<List<HomeBannerInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.city = city;
        this.district = district;
    }

    @Override
    protected List<HomeBannerInfo> onDumpData(JSONObject responseJson) {
        JSONArray jsonArray = responseJson.optJSONArray("data");
        return JsonHelper.toList(jsonArray,HomeBannerInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/selectGuangGaoByAddressID");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("addressname_two",city);
        params.put("addressname_three",district);
    }
}
