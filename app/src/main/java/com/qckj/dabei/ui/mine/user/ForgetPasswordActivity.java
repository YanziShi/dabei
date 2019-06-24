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
import com.qckj.dabei.manager.mine.user.ForgetPasswordRequester;
import com.qckj.dabei.manager.mine.user.GetVerificationCodeRequester;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * 忘记密码界面
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @FindViewById(R.id.input_phone)
    private EditText mInputPhoneNum;
    @FindViewById(R.id.input_verification_code)
    private EditText mVerCode;
    @FindViewById(R.id.input_new_password)
    private EditText mNewPwd;
    @FindViewById(R.id.input_confirm_new_password)
    private EditText mConfirmNewPwd;
    @FindViewById(R.id.get_verification_code)
    private TextView textGetCode;


    private int time = 60;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (time > 0) {
                handler.sendEmptyMessageDelayed(0, 1000);
                textGetCode.setText(getString(R.string.mine_verification_code_time, time));
                time--;
            } else {
                textGetCode.setEnabled(true);
                textGetCode.setText(R.string.mine_verification_code_again);
                time = 60;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ViewInject.inject(this);
    }

    @OnClick({R.id.get_verification_code, R.id.confirm_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                getVerificationCode();
                break;
            case R.id.confirm_btn:
                confirmModifyPwd();
                break;
        }
    }

    private void confirmModifyPwd() {
        String phoneNumber = mInputPhoneNum.getText().toString().trim();
        String verCode = mVerCode.getText().toString().trim();
        String newPwd = mNewPwd.getText().toString();
        String confirmNewPwd = mConfirmNewPwd.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("手机号不能为空");
        } else if (phoneNumber.length() != 11) {
            showToast("手机号不足11位");
        } else if (TextUtils.isEmpty(verCode)) {
            showToast("请输入验证码");
        } else if (TextUtils.isEmpty(newPwd)) {
            showToast("请输入新密码");
        } else if (TextUtils.isEmpty(confirmNewPwd)) {
            showToast("请再次输入新密码");
        } else if (!newPwd.equals(confirmNewPwd)) {
            showToast("两次密码不一致");
        } else {
            confirmModify(phoneNumber, verCode, newPwd, confirmNewPwd);
        }
    }

    private void confirmModify(String phoneNumber, String verCode, String newPwd, String confirmNewPwd) {
        showLoadingProgressDialog();
        new ForgetPasswordRequester(phoneNumber, verCode, newPwd, confirmNewPwd, new OnHttpResponseCodeListener<String>() {
            @Override
            public void onHttpResponse(boolean isSuccess, String s, String message) {
                super.onHttpResponse(isSuccess, s, message);
                dismissLoadingProgressDialog();
                showToast(message);
                if (isSuccess) {
                    finish();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                dismissLoadingProgressDialog();
            }
        }).doPost();
    }

    private void getVerificationCode() {
        String phoneNumber = mInputPhoneNum.getText().toString().trim();
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
                    textGetCode.setEnabled(false);
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
