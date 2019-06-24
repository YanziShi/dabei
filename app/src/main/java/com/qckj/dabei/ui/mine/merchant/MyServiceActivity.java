package com.qckj.dabei.ui.mine.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qckj.dabei.R;
import com.qckj.dabei.ui.home.adapter.MyServiceAdapter;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.ChangeGoodsRequester;
import com.qckj.dabei.manager.mine.merchant.MyServiceRequester;
import com.qckj.dabei.model.merchant.MyServiceInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.List;

import static com.qckj.dabei.view.ActionBar.FUNCTION_TEXT_RIGHT;

/**
 * Created by yangzhizhong on 2019/5/17.
 */
public class MyServiceActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;

    MyServiceAdapter adapter;
    List<MyServiceInfo> datas;

    @Manager
    UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_business_act);
        ViewInject.inject(this);
        adapter = new MyServiceAdapter(this);
        adapter.setListener(new MyServiceAdapter.OnListener() {
            @Override
            public void changeState(String goodsid,String state) {
                new ChangeGoodsRequester(goodsid,state,new OnHttpResponseCodeListener<JSONObject>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                        super.onHttpResponse(isSuccess, jsonObject, message);
                        showToast(message);
                        if(isSuccess) {
                           loadData();
                        }
                    }
                }).doPost();
            }
        });
        pullRefreshView.setAdapter(adapter);

        actionBar.setRightText("添加");
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == FUNCTION_TEXT_RIGHT){
                    AddServiceActivity.startActivity(MyServiceActivity.this);
                }
                return false;
            }
        });

    }

    private void loadData(){
        adapter.setDataNull();
        new MyServiceRequester(userManager.getCurId(), new OnHttpResponseCodeListener<List<MyServiceInfo>>() {
                    @Override
                    public void onHttpResponse(boolean isSuccess, List<MyServiceInfo> myServiceInfos, String message) {
                        super.onHttpResponse(isSuccess, myServiceInfos, message);
                        if (isSuccess && myServiceInfos.size() > 0) {
                            datas= myServiceInfos;
                            adapter.setData(myServiceInfos);
                        }else showToast(message);
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                    }
                }).doPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
