package com.qckj.dabei.manager.mine.user;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.CommonConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.EncryptUtils;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/6/10.
 */
public class GetUserInfoByIdRequester extends SimpleBaseRequester<List<UserByIdInfo>> {

    private String userid;

    public GetUserInfoByIdRequester(String userid, OnHttpResponseCodeListener<List<UserByIdInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userid = userid;
    }

    @Override
    protected List<UserByIdInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserByIdInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/userInformation");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userid);
    }
}
