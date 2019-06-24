package com.qckj.dabei.ui.mine.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.alipay.MyALipayUtils;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.ChoosePayTypeDialog;
import com.qckj.dabei.manager.balance.BalanceUtil;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.CreateGoldOrderRequester;
import com.qckj.dabei.manager.mine.merchant.GoldMerchantsRequester;
import com.qckj.dabei.model.merchant.GoldMerchantsInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.wxapi.WXPayEntryActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/21.
 */
public class GoldMerchantActivity extends BaseActivity {
    @FindViewById(R.id.text_hint)
    TextView textHint;

    @FindViewById(R.id.text_title)
    TextView textTitle;

    @FindViewById(R.id.text_price_time)
    TextView textPriceTime;

    @FindViewById(R.id.text_intro)
    TextView textIntro;

    @FindViewById(R.id.image_gold)
    ImageView imageGold;

    @FindViewById(R.id.btn_pay)
    Button btnPay;

    @Manager
    UserManager userManager;
    GoldMerchantsInfo data;
    double balance;

    public static void startActivity(Context context, int isGold, double balance) {
        Intent intent = new Intent(context, GoldMerchantActivity.class);
        intent.putExtra("isGold", isGold);
        intent.putExtra("balance", balance);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_merchant);
        ViewInject.inject(this);
        EventBus.getDefault().register(this);
        if (getIntent().getIntExtra("isGold", 0) == 1) btnPay.setText("立即续费");
        else btnPay.setText("立即开通");
        balance = getIntent().getDoubleExtra("balance", 0);
    }

    @OnClick({R.id.btn_pay})
    private void onClickView(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                ChoosePayTypeDialog dlg = new ChoosePayTypeDialog(this);
                dlg.show();
                dlg.setData(data.getCost(), data.getUnit(), balance);
                dlg.setListener(new ChoosePayTypeDialog.OnListener() {
                    @Override
                    public void select(int type, double cost) {
                        new CreateGoldOrderRequester(userManager.getCurId(), data.getId(), new OnHttpResponseCodeListener<JSONObject>() {
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                if (isSuccess) {
                                    try {
                                        String orderCode = jsonObject.getString("order_code");
                                        if (type == 0)
                                            BalanceUtil.balancePay(getActivity(), BalanceUtil.PAY_GOLD, orderCode);
                                        else if (type == 1)
                                            WXPayEntryActivity.weixinPay(getActivity(), BalanceUtil.PAY_GOLD, orderCode, cost);
                                        else
                                            MyALipayUtils.alyPay(getActivity(), BalanceUtil.PAY_GOLD, orderCode, cost);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                } else showToast(message);
                            }
                        }).doPost();
                    }
                });
                break;
        }
    }

    void loadData() {
        showLoadingProgressDialog();
        new GoldMerchantsRequester(userManager.getCurId(), new OnHttpResponseCodeListener<List<GoldMerchantsInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<GoldMerchantsInfo> goldMerchantsInfos, String message) {
                super.onHttpResponse(isSuccess, goldMerchantsInfos, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    data = goldMerchantsInfos.get(0);
                    Glide.with(GoldMerchantActivity.this).load(data.getUrl()).into(imageGold);
                    textTitle.setText(data.getName());
                    textPriceTime.setText(data.getCost() + "元/" + data.getUnit());
                    textIntro.setText(data.getIntro());
                    textHint.setText(data.getHint());
                } else showToast(message);
            }
        }).doPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        MobclickAgent.onResume(this);
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
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
