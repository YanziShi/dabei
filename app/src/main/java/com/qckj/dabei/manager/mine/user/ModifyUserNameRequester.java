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
 * 修改用户姓名请求
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ModifyUserNameRequester extends SimpleBaseRequester<List<UserByIdInfo>> {

    private String name;
    private String userId;

    public ModifyUserNameRequester(String name, String userId, OnHttpResponseCodeListener<List<UserByIdInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.name = name;
        this.userId = userId;
    }

    @Override
    protected List<UserByIdInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), UserByIdInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/editUserNicName");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("nicheng", name);
        params.put("userid", userId);
    }

    /**
     * 修改登录密码
     * <p>
     * Created by yangzhizhong on 2019/4/8.
     */
    public static class ModifyLoginPasswordRequester extends SimpleBaseRequester<List<UserInfo>> {

        private String oldPwd;
        private String newPwd;
        private String conNewPwd;
        private String userId;

        public ModifyLoginPasswordRequester(String oldPwd, String newPwd, String conNewPwd, String userId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
            super(onHttpResponseCodeListener);
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
            this.conNewPwd = conNewPwd;
            this.userId = userId;
        }

        @Override
        protected List<UserInfo> onDumpData(JSONObject jsonObject) {
            return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
        }

        @NonNull
        @Override
        public String getServerUrl() {
            return SystemConfig.getServerUrl("/alterPassword");
        }

        @Override
        protected void onPutParams(Map<String, Object> params) {
            params.put("password", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(oldPwd)));
            params.put("newPassword", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(newPwd)));
            params.put("setPassword", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(conNewPwd)));
            params.put("userid", userId);
        }
    }

    /**
     * 设置登录密码
     * <p>
     * Created by yangzhizhong on 2019/4/8.
     */
    public static class SetLoginPasswordRequester extends SimpleBaseRequester<List<UserInfo>> {
        private String newPwd;
        private String conNewPwd;
        private String userId;

        public SetLoginPasswordRequester(String newPwd, String conNewPwd, String userId, OnHttpResponseCodeListener<List<UserInfo>> onHttpResponseCodeListener) {
            super(onHttpResponseCodeListener);
            this.newPwd = newPwd;
            this.conNewPwd = conNewPwd;
            this.userId = userId;
        }

        @Override
        protected List<UserInfo> onDumpData(JSONObject jsonObject) {
            return JsonHelper.toList(jsonObject.optJSONArray("data"), UserInfo.class);
        }

        @NonNull
        @Override
        public String getServerUrl() {
            return SystemConfig.getServerUrl("/setPassword");
        }

        @Override
        protected void onPutParams(Map<String, Object> params) {
            params.put("newPassword", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(newPwd)));
            params.put("setPassword", EncryptUtils.encryptData(CommonConfig.aesconfig, EncryptUtils.MD5(conNewPwd)));
            params.put("userid", userId);
        }
    }
}
