package com.qckj.dabei.ui.mine.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.alipay.MyALipayUtils;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.ChoosePayTypeDialog;
import com.qckj.dabei.manager.balance.BalanceUtil;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.CreateTopOrderRequester;
import com.qckj.dabei.manager.mine.merchant.ShopTopRequester;
import com.qckj.dabei.model.merchant.ShopTopInfo;
import com.qckj.dabei.ui.mine.merchant.adapter.ShopTopGridAdapter;
import com.qckj.dabei.ui.mine.merchant.adapter.ShopTopListAdapter;
import com.qckj.dabei.view.AddressPicker;
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
 * Created by yangzhizhong on 2019/5/20.
 */
public class ShopTopActivity extends BaseActivity {

    @FindViewById(R.id.list_view)
    ListView listView;

    @FindViewById(R.id.grid_view)
    GridView gridView;

    @FindViewById(R.id.text_city)
    TextView textCity;

    ShopTopListAdapter listAdapter;
    ShopTopGridAdapter gridAdapter;
    List<ShopTopInfo> datas;
    @Manager
    UserManager userManager;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;
    UserLocationInfo userLocationInfo;
    ShopTopInfo shopTopInfo;

    String orderCode;
    double balance;

    public static void startActivity(Context context, double balance) {
        Intent intent = new Intent(context, ShopTopActivity.class);
        intent.putExtra("balance", balance);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_top);
        ViewInject.inject(this);
        EventBus.getDefault().register(this);
        userLocationInfo = gaoDeLocationManager.getUserLocationInfo();
        balance = getIntent().getDoubleExtra("balance", 0);
        listAdapter = new ShopTopListAdapter(this);
        listView.setAdapter(listAdapter);
        gridAdapter = new ShopTopGridAdapter(this);
        gridView.setAdapter(gridAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shopTopInfo = datas.get(position);
            }
        });
        String adcode = userLocationInfo.getProvince() + "-" + userLocationInfo.getCity() + "-" + userLocationInfo.getDistrict();
        textCity.setText(adcode);

    }

    @OnClick({R.id.btn_top, R.id.text_city})
    private void onClickView(View v) {
        switch (v.getId()) {
            case R.id.text_city:
                AddressPicker.showAddrPicker(ShopTopActivity.this, new AddressPicker.ClickListener() {
                    @Override
                    public void onClick(String address) {
                        textCity.setText(address);
                    }
                });
                break;
            case R.id.btn_top:
                if (shopTopInfo == null) {
                    showToast("请选择置顶金额！");
                } else {
                    ChoosePayTypeDialog dlg = new ChoosePayTypeDialog(getActivity());
                    dlg.show();
                    dlg.setData(shopTopInfo.getTopPrice(), shopTopInfo.getTopTimeName(), balance);
                    dlg.setListener(new ChoosePayTypeDialog.OnListener() {
                        @Override
                        public void select(int type, double cost) {
                            new CreateTopOrderRequester(userManager.getCurId(), shopTopInfo.getTopId(), new OnHttpResponseCodeListener<JSONObject>() {
                                @Override
                                public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                    super.onHttpResponse(isSuccess, jsonObject, message);
                                    if (isSuccess) {
                                        try {
                                            orderCode = jsonObject.getString("order_code");
                                            if (type == 0)
                                                BalanceUtil.balancePay(getActivity(), BalanceUtil.PAY_TOP, orderCode);
                                            else if (type == 1)
                                                WXPayEntryActivity.weixinPay(getActivity(), BalanceUtil.PAY_TOP, orderCode, cost);
                                            else
                                                MyALipayUtils.alyPay(getActivity(), BalanceUtil.PAY_TOP, orderCode, cost);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else showToast(message);
                                }
                            }).doPost();
                        }
                    });
                }
                break;
        }
    }

    void loadData() {
        showLoadingProgressDialog();
        datas = null;
        shopTopInfo = null;
        listAdapter.setData(null);
        gridAdapter.setData(null);
        new ShopTopRequester(userManager.getCurId(), textCity.getText().toString(),
                new OnHttpResponseCodeListener<List<ShopTopInfo>>() {
                    @Override
                    public void onHttpResponse(boolean isSuccess, List<ShopTopInfo> shopTopInfos, String message) {
                        super.onHttpResponse(isSuccess, shopTopInfos, message);
                        dismissLoadingProgressDialog();
                        if (isSuccess) {
                            datas = shopTopInfos;
                            listAdapter.setData(datas);
                            gridAdapter.setData(datas);
                        } else showToast(message);
                    }
                }).doPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        loadData();
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
