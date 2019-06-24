package com.qckj.dabei.model.street;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/8.
 */
public class BusinessCircleRequester extends SimpleBaseRequester<List<BusinessCircleInfo>> {

    private String user_id;
    private String street_id;
    private String keyword;

    public BusinessCircleRequester(String user_id,String street_id,String keyword, OnHttpResponseCodeListener<List<BusinessCircleInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.user_id = user_id;
        this.street_id = street_id;
        this.keyword = keyword;
    }

    @Override
    protected List<BusinessCircleInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), BusinessCircleInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getBusinessdistrictByqy");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", user_id);
        params.put("street_id", street_id);
        params.put("keyword", keyword);
    }
}
