package com.qckj.dabei.ui.mine.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.contact.AddEmergencyContactRequest;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * 紧急联系人添加
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class EmergencyContactAddActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    private ActionBar mActionBar;

    @FindViewById(R.id.input_contact_name)
    private EditText mInputContactName;

    @FindViewById(R.id.input_contact_phone)
    private EditText mInputContactPhone;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EmergencyContactAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_add);
        ViewInject.inject(this);
        initListener();
    }

    private void initListener() {
        mActionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if (function == ActionBar.FUNCTION_TEXT_RIGHT) {
                    addContact();
                    return true;
                }
                return false;
            }
        });

    }

    // 添加联系人
    private void addContact() {
        String contactName = mInputContactName.getText().toString().trim();
        String contactPhone = mInputContactPhone.getText().toString().trim();
        if (TextUtils.isEmpty(contactName)) {
            showToast(R.string.mine_emergency_contact_empty);
            return;
        }
        if (TextUtils.isEmpty(contactPhone)) {
            showToast(R.string.mine_emergency_phone_empty);
            return;
        }
        if (contactPhone.length() != 11) {
            showToast(R.string.mine_emergency_phone_error);
            return;
        }

        String userId = userManager.getCurId();
        showLoadingProgressDialog();
        new AddEmergencyContactRequest(userId, contactName, contactPhone, new OnHttpResponseCodeListener<String>() {
            @Override
            public void onHttpResponse(boolean isSuccess, String result, String message) {
                super.onHttpResponse(isSuccess, result, message);
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
