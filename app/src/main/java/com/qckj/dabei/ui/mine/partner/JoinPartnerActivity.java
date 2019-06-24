package com.qckj.dabei.ui.mine.partner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qckj.dabei.R;
import com.qckj.dabei.alipay.MyALipayUtils;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.ChoosePayTypeDialog;
import com.qckj.dabei.manager.balance.BalanceUtil;
import com.qckj.dabei.manager.mine.partner.CreateMemberOrderRequester;
import com.qckj.dabei.manager.mine.partner.GetMemberInfoRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.mine.MemberInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.qckj.dabei.wxapi.WXPayEntryActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 加入合伙人
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class JoinPartnerActivity extends BaseActivity {

    @FindViewById(R.id.list_view)
    private ListView listView;

    @Manager
    UserManager userManager;

    private MemberAdapter memberAdapter;
    MemberInfo mMemberInfo ;
    List<MemberInfo> mMemberInfos;
    double balance;

    public static void startActivity(Context context,int ishhr,double balance) {
        Intent intent = new Intent(context, JoinPartnerActivity.class);
        intent.putExtra("ishhr",ishhr);
        intent.putExtra("balance",balance);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_partner);
        ViewInject.inject(this);
        EventBus.getDefault().register(this);
        balance = getIntent().getDoubleExtra("balance",0);
        initListener();
        loadData();

    }

    private void initListener() {
        memberAdapter = new MemberAdapter(this);
        listView.setAdapter(memberAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMemberInfo = mMemberInfos.get(position);
            }
        });
    }

    private void loadData() {
        showLoadingProgressDialog();
        new GetMemberInfoRequester(userManager.getCurId(),1,10,new OnHttpResponseCodeListener<List<MemberInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<MemberInfo> memberInfos, String message) {
                super.onHttpResponse(isSuccess, memberInfos, message);
                dismissLoadingProgressDialog();
                if(isSuccess){
                    mMemberInfos = memberInfos;
                    memberAdapter.setData(memberInfos);
                }else showToast(message);
            }
        }).doPost();
    }

    @OnClick({R.id.btn_join_protocol,R.id.btn_sure})
    void onViewClick(View view){
        switch (view.getId()){
            case R.id.btn_join_protocol:
                String url = SystemConfig.webUrl+ "/#/partnerProtocol";
                BrowserActivity.startActivity(JoinPartnerActivity.this,url,"合伙人协议");
                break;
            case R.id.btn_sure:
                if(getIntent().getIntExtra("ishhr",0)==1){
                    showToast("您已是会员,请勿重复开通！");
                    return;
                }
                if(mMemberInfo==null) {
                    showToast("请选择会员等级！");
                    return;
                }
                showLoadingProgressDialog();
                new CreateMemberOrderRequester(userManager.getCurId(),mMemberInfo.getId(),new OnHttpResponseCodeListener<JSONObject>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                        super.onHttpResponse(isSuccess, jsonObject, message);
                        dismissLoadingProgressDialog();
                        if(isSuccess){
                            ChoosePayTypeDialog dlg = new ChoosePayTypeDialog(JoinPartnerActivity.this);
                            dlg.show();
                            dlg.setData(Double.valueOf(mMemberInfo.getMemberPrice()),null,balance);
                            dlg.setListener(new ChoosePayTypeDialog.OnListener() {
                                @Override
                                public void select(int type,double cost) {
                                    try {
                                        String orderCode = jsonObject.getString("order_code");
                                        if(type ==0) BalanceUtil.balancePay(getActivity(),BalanceUtil.PAY_VIP,orderCode);
                                        else if(type == 1) WXPayEntryActivity.weixinPay(getActivity(),BalanceUtil.PAY_VIP,orderCode,cost);
                                        else MyALipayUtils.alyPay(getActivity(),BalanceUtil.PAY_VIP,orderCode,cost);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else new AlertDialog.Builder(JoinPartnerActivity.this).setTitle(message).setNegativeButton("确定",null).show();
                    }
                }).doPost();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String bundle) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
