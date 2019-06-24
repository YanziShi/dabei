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
 * Created by yangzhizhong on 2019/5/7.
 */
public class StreetRequester extends SimpleBaseRequester<List<StreetInfo>> {

    private String userid;
    private String adcode;
    private String keyword;

    public StreetRequester(String userid,String adcode,String keyword, OnHttpResponseCodeListener<List<StreetInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userid = userid;
        this.adcode = adcode;
        this.keyword = keyword;
    }

    @Override
    protected List<StreetInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), StreetInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/queryStreetByqy");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userid);
        params.put("adcode", adcode);
        params.put("keyword", keyword);
    }
}
