package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.ServiceDetailInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/7.
 */
public class ServiceDetailRequester extends SimpleBaseRequester<List<ServiceDetailInfo>> {

    private String id;

    public ServiceDetailRequester(String id, OnHttpResponseCodeListener<List<ServiceDetailInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.id = id;
    }

    @Override
    protected List<ServiceDetailInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), ServiceDetailInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/goodsService");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("goodsid", id);
    }
}
