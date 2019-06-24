package com.qckj.dabei.ui.mine.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.order.EvaluationRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/6/2.
 */
public class EvaluationActivity extends BaseActivity {

    @FindViewById(R.id.ratingBar)
    RatingBar ratingBar;
    @FindViewById(R.id.edtComment)
    EditText edtComment;

    String userOrderId;
    @Manager
    UserManager userManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ViewInject.inject(this);
        userOrderId = getIntent().getStringExtra("userOrderId");
    }

    @OnClick(R.id.btnComment)
    void onViewClick(View view){
        new EvaluationRequester(userManager.getCurId(),userOrderId,edtComment.getText().toString(),(int)ratingBar.getRating(),
                new OnHttpResponseCodeListener<JSONObject>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                        super.onHttpResponse(isSuccess, jsonObject, message);
                        showToast(message);
                        if (isSuccess) finish();
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
