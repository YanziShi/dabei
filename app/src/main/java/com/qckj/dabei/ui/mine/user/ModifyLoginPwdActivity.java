package com.qckj.dabei.ui.mine.user;

import android.app.Activity;
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
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改登录密码接口
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ModifyLoginPwdActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.group_old)
    private View groupOld;

    @FindViewById(R.id.input_old_password)
    private EditText mOldPwd;

    @FindViewById(R.id.input_new_password)
    private EditText mNewPwd;

    @FindViewById(R.id.input_confirm_new_password)
    private EditText mConfirmNewPwd;

    @Manager
    private UserManager userManager;

    int pwdState;

    public static void startActivity(Activity context, int pwdState,int requestCode) {
        Intent intent = new Intent(context, ModifyLoginPwdActivity.class);
        intent.putExtra("pwdState",pwdState);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_login_pwd);
        ViewInject.inject(this);
        pwdState = getIntent().getIntExtra("pwdState",0);
        if(pwdState==1){
            actionBar.setTitleText("修改密码");
            groupOld.setVisibility(View.VISIBLE);
        }else{
            actionBar.setTitleText("设置密码");
            groupOld.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.confirm_btn)
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.confirm_btn:
                if(pwdState==1)modifyPwd();
                else setPwd();
                break;
        }
    }

    private void setPwd() {
        String newPwd = mNewPwd.getText().toString();
        String conNewPwd = mConfirmNewPwd.getText().toString();
        if (TextUtils.isEmpty(newPwd)) {
            showToast("新密码不能为空");
        } else if (TextUtils.isEmpty(conNewPwd)) {
            showToast("确认新密码不能为空");
        } else if (!newPwd.equals(conNewPwd)) {
            showToast("两次新密码不一致");
        } else {
            showLoadingProgressDialog();
            userManager.setLoginPwd(newPwd, conNewPwd, new OnResultMessageListener() {
                @Override
                public void onResult(boolean isSuccess, String message) {
                    dismissLoadingProgressDialog();
                    if (isSuccess) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        showToast(message);
                    }
                }
            });
        }
    }

    private void modifyPwd() {
        String oldPwd = mOldPwd.getText().toString();
        String newPwd = mNewPwd.getText().toString();
        String conNewPwd = mConfirmNewPwd.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            showToast("旧密码不能为空");
        } else if (TextUtils.isEmpty(newPwd)) {
            showToast("新密码不能为空");
        } else if (TextUtils.isEmpty(conNewPwd)) {
            showToast("确认新密码不能为空");
        } else if (!newPwd.equals(conNewPwd)) {
            showToast("两次新密码不一致");
        } else {
            showLoadingProgressDialog();
            userManager.modifyLoginPwd(oldPwd, newPwd, conNewPwd, new OnResultMessageListener() {
                @Override
                public void onResult(boolean isSuccess, String message) {
                    dismissLoadingProgressDialog();
                    if (isSuccess) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        showToast(message);
                    }
                }
            });
        }
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
