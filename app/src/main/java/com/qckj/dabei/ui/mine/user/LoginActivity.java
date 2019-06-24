package com.qckj.dabei.ui.mine.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.user.ThirdLoginRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

/**
 * 登录界面
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class LoginActivity extends BaseActivity {

    @FindViewById(R.id.other_login_view)
    private LinearLayout otherLoginView;

    @Manager
    private UserManager userManager;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.overridePendingTransition(R.anim.move_bottom_in_avtivity, R.anim.fade_out_activity);
    }

    private UserManager.OnUserLoginListener onUserLoginListener = new UserManager.OnUserLoginListener() {
        @Override
        public void onLoginSuccess(UserInfo userInfo) {
            finish();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String bundle) {
        String openid = bundle;
        loginThirdPlatformNext(SHARE_MEDIA.WEIXIN,openid);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewInject.inject(this);
        userManager.addOnUserLoginListener(onUserLoginListener);
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userManager.removeOnUserLoginListener(onUserLoginListener);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.login_rl, R.id.mine_go_go, R.id.login_account_password, R.id.login_verification_code,
            R.id.other_login_ll, R.id.qq_login_ll, R.id.wx_login_ll, R.id.cancel,R.id.text_protocol})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.login_rl:
                isDealOtherLoginView();
                break;
            case R.id.mine_go_go:
                if (!isDealOtherLoginView()) {
                    finish();
                }
                break;
            case R.id.login_account_password:
                if (!isDealOtherLoginView()) {
                    // 跳转账号登录界面
                    AccountLoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.login_verification_code:
                if (!isDealOtherLoginView()) {
                    // 跳转验证码登录界面
                    VerificationCodeLoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.other_login_ll:
                if (!isDealOtherLoginView()) {
                    otherLoginView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.qq_login_ll:
                loginThirdPlatform(SHARE_MEDIA.QQ);
                break;
            case R.id.wx_login_ll:
                loginThirdPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.cancel:
                otherLoginView.setVisibility(View.GONE);
                break;
            case R.id.text_protocol:
                String url = SystemConfig.webUrl+ "/#/loginProtocol";
                BrowserActivity.startActivity(LoginActivity.this,url,"大贝用户服务协议");
                break;
        }
    }

    private void loginThirdPlatform(SHARE_MEDIA shareMedia) {
        UMLoginApp.loginApp(getActivity(), shareMedia, new OnResult<Map<String, String>>() {
            @Override
            public void onResult(boolean isSuccess, Map<String, String> map) {
                otherLoginView.setVisibility(View.GONE);
                if (isSuccess) {
                    String openid = map.get("openid");
                    loginThirdPlatformNext(shareMedia,openid);
                }
            }
        });
    }

    private void loginThirdPlatformNext(SHARE_MEDIA shareMedia,String openid){
        showLoadingProgressDialog();
        userManager.loginThirdPlatform(shareMedia, openid, new OnResultMessageListener() {
            @Override
            public void onResult(boolean isSuccess, String message) {
                dismissLoadingProgressDialog();
                if (!isSuccess) {
                    if (UserManager.UN_BIND_PHONE.equals(message)) {
                        BindPhoneActivity.startActivity(getActivity(), openid);
                    } else {
                        showToast(message);
                    }
                }else finish();
            }
        });
    }

    private boolean isDealOtherLoginView() {
        if (otherLoginView.getVisibility() == View.VISIBLE) {
            otherLoginView.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
