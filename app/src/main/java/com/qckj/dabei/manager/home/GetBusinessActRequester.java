package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取首页精品信息
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class GetBusinessActRequester extends SimpleBaseRequester<List<HomeBoutiqueRecommendInfo>> {
    int rows;
    int page;
    String city;
    String district;

    public GetBusinessActRequester(int rows,int page, String city, String district, OnHttpResponseCodeListener<List<HomeBoutiqueRecommendInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.rows =rows;
        this.page = page;
        this.city = city;
        this.district = district;
    }

    @Override
    protected List<HomeBoutiqueRecommendInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), HomeBoutiqueRecommendInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getActivityByqy");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page",page);
        params.put("rows",rows);
        params.put("city",city);
        params.put("district",district);
    }
}
