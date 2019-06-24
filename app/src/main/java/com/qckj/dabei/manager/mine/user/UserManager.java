package com.qckj.dabei.manager.mine.user;

import android.text.TextUtils;

import com.qckj.dabei.BuildConfig;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.json.JsonHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息管理类
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class UserManager extends BaseManager {

    public static final String UN_BIND_PHONE = "un_bind_phone";

    private SettingManager settingManager;
    private UserInfo mUserInfo;

    private List<OnUserLoginListener> onUserLoginListeners = new ArrayList<>();

    @Override
    public void onManagerCreate(App app) {
        settingManager = getManager(SettingManager.class);
        loadUserInfo();
    }

    public void loginVerificationCode(String phoneNumber, String code, OnResultMessageListener onResultListener) {
        new LoginVerificationCodeRequester(phoneNumber, code, new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultListener.onResult(isSuccess, message);
                if (isSuccess) {
                    mUserInfo = userInfos.get(0);
                    saveUserInfo();
                    notifyUserLogin(mUserInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultListener.onResult(false, "");
            }
        }).doPost();
    }

    public void loginPhonePwd(String phoneNumber, String password, OnResultMessageListener onResultListener) {
        new LoginPhonePwdRequester(phoneNumber, password, new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultListener.onResult(isSuccess, message);
                if (isSuccess) {
                    mUserInfo = userInfos.get(0);
                    saveUserInfo();
                    notifyUserLogin(mUserInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultListener.onResult(false, "");
            }
        }).doPost();
    }

    public void loginThirdPlatform(SHARE_MEDIA shareMedia, String openid, OnResultMessageListener onResultListener) {
        new ThirdLoginRequester(shareMedia == SHARE_MEDIA.QQ ? ThirdLoginRequester.QQ_PLATFORM : ThirdLoginRequester.WEXIN_PLATFORM, openid, new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                if (isSuccess) {
                    UserInfo userInfo = userInfos.get(0);
                    String account = userInfo.getAccount();
                    if (TextUtils.isEmpty(account)) {
                        onResultListener.onResult(false, UN_BIND_PHONE);
                    } else {
                        onResultListener.onResult(true, message);
                        mUserInfo = userInfos.get(0);
                        saveUserInfo();
                        notifyUserLogin(mUserInfo);
                    }
                } else {
                    onResultListener.onResult(false, message);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultListener.onResult(false, "");
            }
        }).doPost();

    }


    public void bindPhone(String phoneNum, String code, String openId, OnResultMessageListener onResultMessageListener) {

        new BindPhoneRequester(phoneNum, code, openId, new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultMessageListener.onResult(isSuccess, message);
                if (isSuccess) {
                    mUserInfo = userInfos.get(0);
                    saveUserInfo();
                    notifyUserLogin(mUserInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultMessageListener.onResult(false, "");
            }
        }).doPost();
    }

    // 修改用户姓名
    public void modifyUserName(String name, OnResultMessageListener onResultMessageListener) {
        new ModifyUserNameRequester(name, getCurId(), new OnHttpResponseCodeListener<List<UserByIdInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserByIdInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultMessageListener.onResult(isSuccess, message);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultMessageListener.onResult(false, "");
            }
        }).doPost();

    }

    // 修改用户姓名
    public void modifyUserSex(boolean isMale, OnResultMessageListener onResultMessageListener) {
        int sex;
        if (isMale) {
            sex = UserInfo.SEX_MAIL;
        } else {
            sex = UserInfo.SEX_FEMAIL;
        }
        new ModifyUserSexRequester(sex, getCurId(), new OnHttpResponseCodeListener<List<UserByIdInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserByIdInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultMessageListener.onResult(isSuccess, message);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultMessageListener.onResult(false, "");
            }
        }).doPost();

    }

    // 重新绑定手机号
    public void againBindPhone(String name, String code, OnResultMessageListener onResultMessageListener) {
        new AgainBindPhoneRequester(name, code, getCurId(), new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultMessageListener.onResult(isSuccess, message);
                if (isSuccess) {
                    mUserInfo = userInfos.get(0);
                    saveUserInfo();
                    notifyUserLogin(mUserInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultMessageListener.onResult(false, "");
            }
        }).doPost();

    }

    // 修改登录密码
    public void modifyLoginPwd(String oldPwd, String newPwd, String conNewPwd, OnResultMessageListener onResultMessageListener) {
        new ModifyUserNameRequester.ModifyLoginPasswordRequester(oldPwd, newPwd, conNewPwd, getCurId(), new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultMessageListener.onResult(isSuccess, message);
                if (isSuccess) {
                    mUserInfo = userInfos.get(0);
                    saveUserInfo();
                    notifyUserLogin(mUserInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultMessageListener.onResult(false, "");
            }
        }).doPost();

    }

    // 修改登录密码
    public void setLoginPwd( String newPwd, String conNewPwd, OnResultMessageListener onResultMessageListener) {
        new ModifyUserNameRequester.SetLoginPasswordRequester(newPwd, conNewPwd, getCurId(), new OnHttpResponseCodeListener<List<UserInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserInfo> userInfos, String message) {
                super.onHttpResponse(isSuccess, userInfos, message);
                onResultMessageListener.onResult(isSuccess, message);
                if (isSuccess) {
                    mUserInfo = userInfos.get(0);
                    saveUserInfo();
                    notifyUserLogin(mUserInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                onResultMessageListener.onResult(false, "");
            }
        }).doPost();

    }

    public void logout() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId("");
        mUserInfo = userInfo;
        saveUserInfo();
    }

    // 判断用户是否登录
    public boolean isLogin() {
        return !TextUtils.isEmpty(mUserInfo.getId());
    }

    // 判断用户是否登录
    public String getCurId() {
        return mUserInfo.getId();
    }

    private void loadUserInfo() {
        String userInfoJson = settingManager.getSetting(SettingManager.KEY_USER_INFO, "");
        if (TextUtils.isEmpty(userInfoJson)) {
            mUserInfo = new UserInfo();
        } else {
            try {
                mUserInfo = JsonHelper.toObject(new JSONObject(userInfoJson), UserInfo.class);
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException("启动加载用户信息失败");
            }
        }
        if (BuildConfig.DEBUG) {
            logger.i("用户信息：%s", mUserInfo);
        }
    }


    public void saveUserInfo() {
        try {
            settingManager.putSetting(SettingManager.KEY_USER_INFO, new UserInfo().toJsonObject(mUserInfo).toString());
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("用户信息保存失败");
        }
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void notifyUserLogin(UserInfo userInfo) {
        for (OnUserLoginListener onUserLoginListener : onUserLoginListeners) {
            onUserLoginListener.onLoginSuccess(userInfo);
        }
    }

    public void addOnUserLoginListener(OnUserLoginListener onUserLoginListener) {
        onUserLoginListeners.add(onUserLoginListener);
    }

    public void removeOnUserLoginListener(OnUserLoginListener onUserLoginListener) {
        onUserLoginListeners.remove(onUserLoginListener);
    }

    public interface OnUserLoginListener {
        void onLoginSuccess(UserInfo userInfo);
    }
}
