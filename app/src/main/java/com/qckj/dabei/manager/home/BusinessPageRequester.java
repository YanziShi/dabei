package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.model.nearby.ServiceInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/13.
 */
public class BusinessPageRequester extends SimpleBaseRequester<List<ServiceInfo>> {

    private int rows;
    private int page;
    private String district;
    private double longitude;
    private double latitude;
    private String classone;
    private String merchantClassification;
    private String condition;
    private String business_district_id;

    public BusinessPageRequester(int rows, int page,  String district, double longitude, double latitude, String classone,
                                 String merchantClassification,String condition,String business_district_id,
                                 OnHttpResponseCodeListener<List<ServiceInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.rows = rows;
        this.page = page;
        this.classone = classone;
        this.merchantClassification = merchantClassification;
        this.district = district;
        this.longitude = longitude;
        this.latitude = latitude;
        this.condition = condition;
        this.business_district_id = business_district_id;
    }

    @Override
    protected List<ServiceInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), ServiceInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getFwsPage");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("classone", classone);
        params.put("rows", rows);
        params.put("page", page);
        params.put("merchantClassification", merchantClassification);
        params.put("district", district);
        params.put("user_x", latitude);
        params.put("user_y", longitude);
        params.put("condition", condition);
        params.put("business_district_id", business_district_id);
    }
}
