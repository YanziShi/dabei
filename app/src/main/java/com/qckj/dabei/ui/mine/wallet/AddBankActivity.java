package com.qckj.dabei.ui.mine.wallet;

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
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.CheckBankCardRequester;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class AddBankActivity extends BaseActivity {

    @FindViewById(R.id.text_name)
    TextView textName;

    @FindViewById(R.id.edit_number)
    EditText editNumber;

    @Manager
    UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddBankActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        ViewInject.inject(this);
        textName.setText("持卡人："+userManager.getUserInfo().getF_C_NAME());

        //editNumber.setText("6217007130000882144");
    }

    @OnClick(R.id.btn_next)
    void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_next:
                if(editNumber.getText().toString().equals("")||editNumber.getText().toString().length()<19) {
                    editNumber.requestFocus();
                    showToast("请正确填写卡号！");
                    return;
                }
                new CheckBankCardRequester(userManager.getCurId(),editNumber.getText().toString(),userManager.getUserInfo().getF_C_NAME(),
                        new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                if(isSuccess){
                                    try {
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        Intent intent = new Intent(AddBankActivity.this,BindBankActivity.class);
                                        intent.putExtra("abbreviation",data.getString("abbreviation"));
                                        intent.putExtra("bankimage",data.getString("bankimage"));
                                        intent.putExtra("bankname",data.getString("bankname"));
                                        intent.putExtra("cardCode",editNumber.getText().toString());
                                        startActivityForResult(intent,0);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else showToast(message);
                            }
                        }).doPost();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            finish();
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
