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
 * 绑定手机号
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class BindPhoneActivity extends BaseActivity {

    public static final String KEY_OPEN_ID = "key_open_id";

    @FindViewById(R.id.input_phone)
    private EditText mInputPhoneNum;

    @FindViewById(R.id.get_verification_code)
    private TextView mVerCode;

    @FindViewById(R.id.input_verification_code)
    private EditText mInputVerCode;

    @Manager
    private UserManager userManager;

    private int time = 60;
    private String openId;

    public static void startActivity(Context context, String openId) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra(KEY_OPEN_ID, openId);
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
                mVerCode.setText(R.string.mine_verification_code_again);
                mVerCode.setEnabled(true);
                time = 60;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ViewInject.inject(this);
        initData();
    }

    private void initData() {
        openId = getIntent().getStringExtra(KEY_OPEN_ID);
    }

    @OnClick({R.id.get_verification_code, R.id.confirm_btn})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                getVerificationCode();
                break;
            case R.id.confirm_btn:
                confirmBindPhone();
                break;
        }
    }

    private void confirmBindPhone() {
        String phoneNumber = mInputPhoneNum.getText().toString().trim();
        String verCode = mInputVerCode.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("手机号不能为空");
        } else if (phoneNumber.length() != 11) {
            showToast("手机号不足11位");
        } else if (TextUtils.isEmpty(verCode)) {
            showToast("请输入验证码");
        } else {
            bindPhone(phoneNumber, verCode);
        }
    }

    private void bindPhone(String phoneNumber, String verCode) {
        showLoadingProgressDialog();
        userManager.bindPhone(phoneNumber, verCode, openId, new OnResultMessageListener() {
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
                    mVerCode.setEnabled(false);
                    handler.sendEmptyMessageDelayed(0, 1000);
                    showToast("获取验证码成功");
                } else showToast("获取验证码失败");
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
