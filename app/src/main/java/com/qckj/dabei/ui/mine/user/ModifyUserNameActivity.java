package com.qckj.dabei.ui.mine.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

/**
 * 修改用户姓名
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ModifyUserNameActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    private ActionBar mActionBar;

    @FindViewById(R.id.input_user_name)
    private EditText mInputUserName;

    @Manager
    private UserManager userManager;

    public static void startActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, ModifyUserNameActivity.class);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_name);
        ViewInject.inject(this);
        initListener();
    }

    private void initListener() {

        mActionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if (function == ActionBar.FUNCTION_TEXT_RIGHT) {
                    modifyName();
                    return true;
                }
                return false;
            }
        });

    }

    private void modifyName() {
        String userName = mInputUserName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            showToast("请输入用户昵称");
            return;
        }
        showLoadingProgressDialog();
        userManager.modifyUserName(userName, new OnResultMessageListener() {
            @Override
            public void onResult(boolean isSuccess, String message) {
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    setResult(RESULT_OK,new Intent().putExtra("userName",userName));
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
