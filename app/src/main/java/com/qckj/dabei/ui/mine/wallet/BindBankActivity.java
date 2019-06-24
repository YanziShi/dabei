package com.qckj.dabei.ui.mine.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.BindBankRequester;
import com.qckj.dabei.manager.mine.wallet.GetVercodeRequester;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class BindBankActivity extends BaseActivity {
    @FindViewById(R.id.text_bank)
    TextView textBank;

    @FindViewById(R.id.text_phone)
    TextView textPhone;

    @FindViewById(R.id.edit_code)
    EditText editCode;

    @FindViewById(R.id.btn_code)
    private Button mVerCode;

    @Manager
    UserManager userManager;

    String bankname;
    String abbreviation;
    String bankimage;
    String cardCode;
    String yzm_id;

    private int time = 60;

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
        setContentView(R.layout.activity_bind_bank);
        ViewInject.inject(this);

        bankimage = getIntent().getStringExtra("bankimage");
        abbreviation = getIntent().getStringExtra("abbreviation");
        bankname = getIntent().getStringExtra("bankname");
        cardCode = getIntent().getStringExtra("cardCode");
        textBank.setText("卡类型："+bankname);
        textPhone.setText("手机号："+userManager.getUserInfo().getAccount());
    }

    @OnClick({R.id.btn_code,R.id.btn_next,R.id.btn_bind_protocol})
    void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_bind_protocol:
                String url = SystemConfig.webUrl+ "/#/bindCardProtocol";
                BrowserActivity.startActivity(BindBankActivity.this,url,"绑定银行卡协议");
                break;
            case R.id.btn_code:
                showLoadingProgressDialog();
                new GetVercodeRequester(userManager.getCurId(),userManager.getUserInfo().getAccount(),new OnHttpResponseCodeListener<JSONObject>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                        super.onHttpResponse(isSuccess, jsonObject, message);
                        dismissLoadingProgressDialog();
                        showToast(message);
                        if(isSuccess) {
                            try {
                                handler.sendEmptyMessageDelayed(0, 1000);
                                mVerCode.setEnabled(false);
                                JSONObject data = jsonObject.getJSONObject("data");
                                yzm_id = data.getString("yzm_id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).doPost();
                break;
            case R.id.btn_next:
                showLoadingProgressDialog();
                new BindBankRequester(userManager.getCurId(),cardCode,yzm_id,editCode.getText().toString(),bankname,abbreviation,userManager.getUserInfo().getF_C_NAME(),
                        new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                dismissLoadingProgressDialog();
                                if(isSuccess){
                                    setResult(RESULT_OK);
                                    finish();
                                }else showToast(message);
                            }
                        }).doPost();

                break;
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
