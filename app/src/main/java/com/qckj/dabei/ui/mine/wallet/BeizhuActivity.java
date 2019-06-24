package com.qckj.dabei.ui.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
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
public class BeizhuActivity extends BaseActivity {

    @FindViewById(R.id.text_num)
    TextView textView;
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @Manager
    UserManager userManager;

    public static void startActivity(Context context ,String number) {
        Intent intent = new Intent(context, BeizhuActivity.class);
        intent.putExtra("number",number);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beizhu);
        ViewInject.inject(this);
        textView.setText(getIntent().getStringExtra("number"));
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    String url = SystemConfig.webUrl+"/#/moneyDetail?type=beizhu&userId="+userManager.getCurId();
                    BrowserActivity.startActivity(BeizhuActivity.this,url,false);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.btn_change)
    void onViewClick(View v){
        switch (v.getId()){
            case R.id.btn_change:
                finish();
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
