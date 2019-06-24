package com.qckj.dabei.ui.mine.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.merchant.PersonalInfoRequester;
import com.qckj.dabei.manager.mine.user.GetUserInfoByIdRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.merchant.PersonalInfo;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 认证中心界面
 * <p>
 * Created by yangzhizhong on 2019/4/16.
 */
public class AuthCenterActivity extends BaseActivity {

    @FindViewById(R.id.btn_auth)
    private Button btnAuth;
    @FindViewById(R.id.btn_auth_self)
    private Button btnPerAuth;
    @FindViewById(R.id.btn_enterprise)
    private Button btnEntAuth;

    @Manager
    UserManager userManager;
    UserByIdInfo userInfo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,AuthCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_center);
        ViewInject.inject(this);

    }

    void loadData(){
        showLoadingProgressDialog();
        new GetUserInfoByIdRequester(userManager.getCurId(),new OnHttpResponseCodeListener<List<UserByIdInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserByIdInfo> userByIdInfos, String message) {
                super.onHttpResponse(isSuccess, userByIdInfos, message);
                dismissLoadingProgressDialog();
                if(isSuccess){
                    userInfo = userByIdInfos.get(0);
                    initUserInfo();
                }
            }
        }).doPost();
    }

    private void initUserInfo() {
        if(userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE_UN){
            btnAuth.setBackgroundResource(R.drawable.auth_center_bg);
            btnAuth.setEnabled(true);
            btnAuth.setText("未认证");
        }else if(userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE){
            btnAuth.setEnabled(true);
            btnAuth.setBackgroundResource(R.drawable.button_orange_corner);
            btnAuth.setText("已认证");
        }else{
            btnAuth.setBackgroundResource(R.drawable.auth_center_bg);
            btnAuth.setEnabled(false);
            btnAuth.setText("认证审核中");
        }

        if(userInfo.getF_I_RZSJ_GR()==UserInfo.USER_AUTH_STATE_UN){
            btnPerAuth.setEnabled(true);
            btnPerAuth.setBackgroundResource(R.drawable.auth_center_bg);
            btnPerAuth.setText("未认证");
        }else if(userInfo.getF_I_RZSJ_GR()== UserInfo.USER_AUTH_STATE){
            btnPerAuth.setEnabled(true);
            btnPerAuth.setBackgroundResource(R.drawable.button_orange_corner);
            btnPerAuth.setText("已认证");
        }else{
            btnPerAuth.setBackgroundResource(R.drawable.auth_center_bg);
            btnPerAuth.setEnabled(false);
            btnPerAuth.setText("认证审核中");
            return;
        }

        if(userInfo.getF_I_RZSJ_QY()==UserInfo.USER_AUTH_STATE_UN){
            btnEntAuth.setBackgroundResource(R.drawable.auth_center_bg);
            btnEntAuth.setEnabled(true);
            btnEntAuth.setText("未认证");
        }else if(userInfo.getF_I_RZSJ_QY()== UserInfo.USER_AUTH_STATE){
            btnEntAuth.setEnabled(true);
            btnEntAuth.setBackgroundResource(R.drawable.button_orange_corner);
            btnEntAuth.setText("已认证");
        }else{
            btnEntAuth.setBackgroundResource(R.drawable.auth_center_bg);
            btnEntAuth.setEnabled(false);
            btnEntAuth.setText("认证审核中");
        }

    }

    @OnClick({R.id.btn_auth,R.id.btn_auth_self,R.id.btn_enterprise})
    private void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_auth:
                if(userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE){
                    showToast("认证已通过！");
                    return;
                }
                RealNameAuthActivity.startActivity(getActivity());
                break;
            case R.id.btn_auth_self:
                if(userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE_UN){
                    showToast("请先进行实名认证！");
                    return;
                }
                if(userInfo.getF_I_RZSJ_QY()== UserInfo.USER_AUTH_STATE){
                    showToast("您已认证企业服务者，不能进行其他认证！");
                    return;
                }
                if(userInfo.getF_I_RZSJ_QY()== UserInfo.USER_AUTH_STATE_ING
                        ||userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE_ING){
                    showToast("您有认证正在审核，请等待审核通过再进行其他认证！");
                    return;
                }
                if(userInfo.getF_I_RZSJ_GR()== UserInfo.USER_AUTH_STATE){
                    showToast("认证已通过！");
                    return;
                }
                PersonAuthActivity.startActivity(getActivity());
                break;
            case R.id.btn_enterprise:
                if(userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE_UN){
                    showToast("请先进行实名认证！");
                    return;
                }
                if(userInfo.getF_I_RZSJ_GR()== UserInfo.USER_AUTH_STATE){
                    showToast("您已认证个人服务者，不能进行其他认证！");
                    return;
                }

                if(userInfo.getF_I_RZSJ_GR()== UserInfo.USER_AUTH_STATE_ING
                        ||userInfo.getF_I_RZSM()== UserInfo.USER_AUTH_STATE_ING){
                    showToast("您有认证正在审核，请等待审核通过再进行其他认证！");
                    return;
                }
                if(userInfo.getF_I_RZSJ_QY()== UserInfo.USER_AUTH_STATE){
                    showToast("认证已通过！");
                    return;
                }
                EnterpriseAuthActivity.startActivity(getActivity());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
