package com.qckj.dabei.ui.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.UnbindBankRequester;
import com.qckj.dabei.model.mine.BankCardInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class UnbindBankActivity extends BaseActivity {

    @FindViewById(R.id.image_bg)
    ImageView imageBg;

    @FindViewById(R.id.image_head)
    ImageView imageHead;

    @FindViewById(R.id.text_name)
    TextView textName;

    @FindViewById(R.id.text_bank_num)
    TextView textBankNum;

    @Manager
    UserManager userManager;
    BankCardInfo bankCardInfo;
    public static void startActivity(Context context, BankCardInfo bankCardInfo) {
        Intent intent = new Intent(context, UnbindBankActivity.class);
        intent.putExtra("bankCardInfo",bankCardInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind_bank);
        ViewInject.inject(this);
        initView();
    }

    void initView(){
        bankCardInfo = (BankCardInfo) getIntent().getSerializableExtra("bankCardInfo");
        Glide.with(this).load(bankCardInfo.getBankBg()).into(imageBg);
        Glide.with(this).load(bankCardInfo.getBankImge()).into(imageHead);
        textName.setText(bankCardInfo.getName());
        textBankNum.setText(bankCardInfo.getBankNumber().substring(bankCardInfo.getBankNumber().length()-3));
    }

    @OnClick(R.id.btn_unbind)
    void onViewClick(View v){
        switch (R.id.btn_unbind){
            case R.id.btn_unbind:
                new UnbindBankRequester(userManager.getCurId(),bankCardInfo.getBankId(),new OnHttpResponseCodeListener<JSONObject>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                        super.onHttpResponse(isSuccess, jsonObject, message);
                        showToast(message);
                        if(isSuccess)finish();
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
