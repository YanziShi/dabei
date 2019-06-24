package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HomeTransactionInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取交易信息请求
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class GetTransactionInfoRequester extends SimpleBaseRequester<List<HomeTransactionInfo>> {

    public GetTransactionInfoRequester(OnHttpResponseCodeListener<List<HomeTransactionInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
    }

    @Override
    protected List<HomeTransactionInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), HomeTransactionInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/businessDynamic");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {

    }
}
