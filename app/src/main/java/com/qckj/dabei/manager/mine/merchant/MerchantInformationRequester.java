package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.MerchantInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/15.
 */
public class MerchantInformationRequester extends SimpleBaseRequester<List<MerchantInfo>> {

    private String userId;

    public MerchantInformationRequester(String userId, OnHttpResponseCodeListener<List<MerchantInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected List<MerchantInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), MerchantInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/facilitatorInformation");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
    }
}
