package com.qckj.dabei.ui.mine.agent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.agent.ApplyAgentRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.view.AddressPicker;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/30.
 */
public class ApplyAgentActivity extends BaseActivity {

    static public void startActivity(Context context){
        Intent intent = new Intent(context,ApplyAgentActivity.class);
        context.startActivity(intent);
    }

    @FindViewById(R.id.action_bar)
    ActionBar actionBar;
    @FindViewById(R.id.edit_name)
    EditText editName;
    @FindViewById(R.id.edit_phone)
    EditText editPhone;
    @FindViewById(R.id.btn_city)
    TextView textCity;

    @Manager
    UserManager userManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_agent);
        ViewInject.inject(this);
        textCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressPicker.showAddrPicker( ApplyAgentActivity.this,new AddressPicker.ClickListener() {
                    @Override
                    public void onClick(String address) {
                        textCity.setText(address);
                    }
                });
            }
        });

        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    showLoadingProgressDialog();
                    new ApplyAgentRequester(userManager.getCurId(),editName.getText().toString(),editPhone.getText().toString(),
                            textCity.getText().toString(),new OnHttpResponseCodeListener<JSONObject>(){
                        @Override
                        public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                            super.onHttpResponse(isSuccess, jsonObject, message);
                            dismissLoadingProgressDialog();
                            showToast(message);
                            if(isSuccess){
                                finish();
                            }
                        }
                    }).doPost();
                }
                return false;
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

