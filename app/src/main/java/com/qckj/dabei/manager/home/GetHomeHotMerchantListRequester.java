package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取热门商家列表
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class GetHomeHotMerchantListRequester extends SimpleBaseRequester<List<HotMerchantInfo>> {

    private int pageSize;
    private int page;
    private String city;
    private String district;
    private double longitude;
    private double latitude;

    public GetHomeHotMerchantListRequester(int pageSize, int page, String city, String district, double longitude, double latitude,
                                           OnHttpResponseCodeListener<List<HotMerchantInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.pageSize = pageSize;
        this.page = page;
        this.city = city;
        this.district = district;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    protected List<HotMerchantInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), HotMerchantInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/businessbyClicknumber");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("rows", pageSize);
        params.put("page", page);
        params.put("addrestwo", city);
        params.put("addresthree", district);
        params.put("user_x", latitude);
        params.put("user_y", longitude);
    }
}
