package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.AuthenticationInfo;
import com.qckj.dabei.model.merchant.ShopTopInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class ShopTopRequester extends SimpleBaseRequester<List<ShopTopInfo> >{

    private String userId;
    private String adcode;

    public ShopTopRequester(String userId,String adcode, OnHttpResponseCodeListener<List<ShopTopInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.adcode = adcode;
    }

    @Override
    protected List<ShopTopInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"),ShopTopInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/getZdList");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("adcode", adcode);
    }
}
