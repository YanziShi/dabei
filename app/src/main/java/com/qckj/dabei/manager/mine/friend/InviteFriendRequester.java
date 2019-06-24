package com.qckj.dabei.manager.mine.friend;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.StartAdvertiseInfo;
import com.qckj.dabei.model.merchant.BusinessShopInfo;
import com.qckj.dabei.model.mine.InviteFriendInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/31.
 */
public class InviteFriendRequester extends SimpleBaseRequester<InviteFriendInfo> {

    private String userId;
    private int page;
    private int pageSize;

    public InviteFriendRequester(String userId,int page, int pageSize,  OnHttpResponseCodeListener<InviteFriendInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    protected InviteFriendInfo onDumpData(JSONObject jsonObject) {
        Gson mGson = new Gson();
        return mGson.fromJson(jsonObject.optJSONObject("data").toString(), InviteFriendInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/getprofitByFrineds");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("page", page);
        params.put("rows", pageSize);
    }
}