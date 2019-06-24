package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.BusinessShopInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/16.
 */
public class BusinessShopRequester extends SimpleBaseRequester<BusinessShopInfo> {

    private String userId;

    public BusinessShopRequester(String userId,OnHttpResponseCodeListener<BusinessShopInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
    }

    @Override
    protected BusinessShopInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toObject(jsonObject.optJSONObject("data"), BusinessShopInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/queryBusinessShop");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
    }
}