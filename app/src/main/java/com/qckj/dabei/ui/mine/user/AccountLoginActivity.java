package com.qckj.dabei.ui.mine.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * 账号登录界面
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class AccountLoginActivity extends BaseActivity {

    @FindViewById(R.id.input_phone)
    private EditText mInputPhone;

    @FindViewById(R.id.input_password)
    private EditText mInputPassword;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AccountLoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        ViewInject.inject(this);
    }

    @OnClick({R.id.back, R.id.password_forget, R.id.login_btn})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackClick(view);
                break;
            case R.id.password_forget:
                ForgetPasswordActivity.startActivity(getActivity());
                break;
            case R.id.login_btn:
                doLogin();
                break;
        }
    }

    private void doLogin() {
        String phoneNumber = mInputPhone.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("手机号码不能为空");
        } else if (phoneNumber.length() != 11) {
            showToast("手机号码不足11位");
        } else if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
        } else {
            login(phoneNumber, password);
        }

    }

    private void login(String phoneNumber, String password) {
        showLoadingProgressDialog();
        userManager.loginPhonePwd(phoneNumber, password, new OnResultMessageListener() {
            @Override
            public void onResult(boolean isSuccess, String message) {
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    finish();
                } else {
                    showToast(message);
                }
            }
        });
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
