package com.qckj.dabei.manager.mine.user;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 上传用户头像
 * <p>
 * Created by yangzhizhong on 2019/3/29.
 */
public class UploadHeadPortraitRequester extends SimpleBaseRequester<List<UserByIdInfo>> {

    private String path;
    private String curUserId;

    public UploadHeadPortraitRequester(String curUserId,String path,  OnHttpResponseCodeListener<List<UserByIdInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.path = path;
        this.curUserId = curUserId;
    }

    @Override
    protected List<UserByIdInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserByIdInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/editUserPhoto");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", curUserId);
        params.put("photo", path);
    }
}
