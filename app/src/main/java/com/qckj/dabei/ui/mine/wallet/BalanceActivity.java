package com.qckj.dabei.ui.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class BalanceActivity extends BaseActivity {

    @FindViewById(R.id.text_num)
    TextView textView;
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;
    @FindViewById(R.id.group_content)
    LinearLayout groupContent;
    @FindViewById(R.id.group_result)
    LinearLayout groupResult;

    @Manager
    UserManager userManager;

    public static void startActivity(Context context , String number) {
        Intent intent = new Intent(context, BalanceActivity.class);
        intent.putExtra("number",number);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        ViewInject.inject(this);
        textView.setText(getIntent().getStringExtra("number"));
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    String url = SystemConfig.webUrl+"/#/moneyDetail?type=balance&userId="+userManager.getCurId();
                    BrowserActivity.startActivity(BalanceActivity.this,url,false);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btn_change)
    void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_change:
                if(MineWalletActivity.cardNum.equals("0")){
                    MsgDialog dlg = new MsgDialog(getActivity());
                    dlg.show("您还没有添加银行卡哦！","","去添加",false);
                    dlg.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.dismiss();
                            AddBankActivity.startActivity(getActivity());
                            finish();
                        }
                    });
                    return;
                }
                Intent intent = new Intent(BalanceActivity.this,BalanceCashActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            groupContent.setVisibility(View.GONE);
            groupResult.setVisibility(View.VISIBLE);
            actionBar.setRightText("");
            actionBar.setTitleText("提现结果");
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
