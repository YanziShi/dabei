package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.RecommendServiceInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取推荐服务信息
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class GetRecommendServiceRequester extends SimpleBaseRequester<List<RecommendServiceInfo>> {

    private String classId;
    private int page;
    private int pageSize;
    private String type;

    public GetRecommendServiceRequester(String classId, int page, int pageSize, String type, OnHttpResponseCodeListener<List<RecommendServiceInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.classId = classId;
        this.page = page;
        this.pageSize = pageSize;
        this.type = type;
    }

    @Override
    protected List<RecommendServiceInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), RecommendServiceInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/personalOrCompany");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("classifyoneId", classId);
        params.put("type", type);
    }
}
