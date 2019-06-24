package com.qckj.dabei.ui.mine.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.user.GetVerificationCodeRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * 验证码登录界面
 * <p>
 * Created by yangzhizhong on 2019/3/27.
 */
public class VerificationCodeLoginActivity extends BaseActivity {

    @FindViewById(R.id.input_phone)
    private EditText mInputPhone;

    @FindViewById(R.id.get_verification_code)
    private TextView mVerCode;

    @FindViewById(R.id.input_verification_code)
    private EditText mInputVerificationCode;

    @Manager
    private UserManager userManager;

    private int time = 60;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, VerificationCodeLoginActivity.class);
        context.startActivity(intent);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (time > 0) {
                handler.sendEmptyMessageDelayed(0, 1000);
                mVerCode.setText(getString(R.string.mine_verification_code_time, time));
                time--;
            } else {
                mVerCode.setEnabled(true);
                mVerCode.setText(R.string.mine_verification_code_again);
                time = 60;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verification_code);
        ViewInject.inject(this);
    }

    @OnClick({R.id.back, R.id.get_verification_code, R.id.login_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackClick(view);
                break;
            case R.id.get_verification_code:
                getVerificationCode();
                break;
            case R.id.login_btn:
                verificationCodeLogin();
                break;
        }
    }

    private void verificationCodeLogin() {
        String phoneNumber = mInputPhone.getText().toString().trim();
        String verificationCode = mInputVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("手机号不能为空");
        } else if (phoneNumber.length() != 11) {
            showToast("手机号不足11位");
        } else if (TextUtils.isEmpty(verificationCode)) {
            showToast("验证码不能为空");
        } else {
            login(phoneNumber, verificationCode);
        }
    }

    private void login(String phoneNumber, String verificationCode) {
        showLoadingProgressDialog();
        userManager.loginVerificationCode(phoneNumber, verificationCode, new OnResultMessageListener() {
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


    private void getVerificationCode() {
        String phoneNumber = mInputPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("手机号不能为空");
        } else if (phoneNumber.length() != 11) {
            showToast("手机号不足11位");
        } else {
            getCode(phoneNumber);
        }
    }

    private void getCode(String phoneNumber) {
        showLoadingProgressDialog();
        new GetVerificationCodeRequester(phoneNumber, new OnHttpResponseCodeListener<String>() {
            @Override
            public void onHttpResponse(boolean isSuccess, String s, String message) {
                super.onHttpResponse(isSuccess, s, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    mVerCode.setEnabled(false);
                    handler.sendEmptyMessageDelayed(0, 1000);
                    showToast("获取验证码成功");
                } else {
                    showToast("获取验证码失败");
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                dismissLoadingProgressDialog();
            }
        }).doPost();
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
