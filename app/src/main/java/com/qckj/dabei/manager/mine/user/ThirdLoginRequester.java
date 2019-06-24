package com.qckj.dabei.manager.mine.user;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.qckj.dabei.app.CommonConfig;
import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.EncryptUtils;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 第三方登录请求
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class ThirdLoginRequester extends SimpleBaseRequester<List<UserInfo>> {

    public static final int QQ_PLATFORM = 1;
    public static final int WEXIN_PLATFORM = 2;

    private int platform;
    private String openId;

    public ThirdLoginRequester(@ThirdLoginPlatform int platform, String openId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.platform = platform;
        this.openId = openId;
    }

    @Override
    protected List<UserInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        if (platform == QQ_PLATFORM) {
            return SystemConfig.getServerUrl("/userQQLogin");
        } else {
            return SystemConfig.getServerUrl("/userWXLogin");
        }
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("openid", EncryptUtils.encryptData(CommonConfig.aesconfig, openId));
    }

    @IntDef({QQ_PLATFORM, WEXIN_PLATFORM})
    public @interface ThirdLoginPlatform {
    }
}
