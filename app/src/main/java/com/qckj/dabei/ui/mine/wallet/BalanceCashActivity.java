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
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.BalanceCashRequester;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/28.
 */
public class BalanceCashActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.select_bank)
    CommonItemView selectBank;

    @FindViewById(R.id.edit_num)
    EditText editNum;

    @FindViewById(R.id.text_balance)
    TextView textBalance;

    @Manager
    UserManager userManager;

    String card_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_cash);
        ViewInject.inject(this);
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    CashRecordActivity.startActivity(BalanceCashActivity.this);
                }
                return false;
            }
        });

        textBalance.setText("可提现余额："+String.valueOf(MineFragment.userInfo.getF_C_BALANCE())+"元");
    }

    @OnClick({R.id.btn_balance,R.id.btn_sure,R.id.select_bank,R.id.btn_cash_protocol})
    void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_cash_protocol:
                String url = SystemConfig.webUrl+ "/#/getMoneyProtocol";
                BrowserActivity.startActivity(BalanceCashActivity.this,url,"提现协议");
                break;
            case R.id.select_bank:
                Intent intent = new Intent(BalanceCashActivity.this, BankCardActivity.class);
                intent.putExtra("isSelecte",true);
                startActivityForResult(intent,0);
                break;
            case R.id.btn_balance:
                editNum.setText(String.valueOf(MineFragment.userInfo.getF_C_BALANCE()));
                break;

            case R.id.btn_sure:
                if(card_id==null){
                    showToast("请选择提现到账的银行卡！");
                    return;
                }

                new BalanceCashRequester(userManager.getCurId(),card_id,editNum.getText().toString(),
                        new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                if(isSuccess){
                                    setResult(RESULT_OK,null);
                                    finish();
                                }else showToast(message);
                            }
                        }).doPost();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            card_id = data.getStringExtra("card_id");
            selectBank.setTitle("提现到:"+data.getStringExtra("card_name"));
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
