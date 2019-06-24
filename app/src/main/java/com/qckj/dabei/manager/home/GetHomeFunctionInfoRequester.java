package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 首页功能信息
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class GetHomeFunctionInfoRequester extends SimpleBaseRequester<List<HomeFunctionInfo>> {

    public GetHomeFunctionInfoRequester(OnHttpResponseCodeListener<List<HomeFunctionInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
    }

    @Override
    protected List<HomeFunctionInfo> onDumpData(JSONObject responseJson) {
        return JsonHelper.toList(responseJson.optJSONArray("data"), HomeFunctionInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/getClassifyOneAndTwo");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }
}
