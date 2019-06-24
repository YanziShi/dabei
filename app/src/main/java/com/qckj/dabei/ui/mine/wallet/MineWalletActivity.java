package com.qckj.dabei.ui.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.wallet.GetGoodsRequester;
import com.qckj.dabei.manager.mine.wallet.GetWalletRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.model.mine.WalletInfo;
import com.qckj.dabei.ui.mine.wallet.adapter.GoodsAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 我的收益
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class MineWalletActivity extends BaseActivity {
    private int curPage = 1;
    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.beibi_num)
    private TextView beibiNum;

    @FindViewById(R.id.balance_num)
    private TextView balanceNum;

    @FindViewById(R.id.bank_num)
    private TextView bankNum;

    @FindViewById(R.id.grid_view)
    private GridView gridView;

    GoodsAdapter adapter;
    static public String cardNum;

    @Manager
    private UserManager userManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineWalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wallet);
        ViewInject.inject(this);
        adapter = new GoodsAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<GoodsInfo>() {
            @Override
            public void onAdapterItemClick(int position, GoodsInfo goodsInfo) {
                Intent intent = new Intent(MineWalletActivity.this,GoodsDetailActivity.class);
                intent.putExtra("goodsInfo",goodsInfo);
                startActivity(intent);
            }
        });
        loadGoods();
    }

    @OnClick({R.id.beibi_ll, R.id.balance_ll, R.id.bank_ll,R.id.image_lucky_draw,R.id.btn_change_record})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.beibi_ll:
                BeizhuActivity.startActivity(MineWalletActivity.this,beibiNum.getText().toString());
                break;
            case R.id.balance_ll:
                BalanceActivity.startActivity(MineWalletActivity.this,balanceNum.getText().toString());
                break;
            case R.id.bank_ll:
                BankCardActivity.startActivity(MineWalletActivity.this);
                break;
            case R.id.image_lucky_draw:
                String url = SystemConfig.webUrl+ "/#/extractionAward?userId="+userManager.getCurId();
                BrowserActivity.startActivity(MineWalletActivity.this,url,false);
                break;
            case R.id.btn_change_record:
                Intent intent = new Intent(MineWalletActivity.this,ChangeRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loadGoods(){
        new GetGoodsRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<GoodsInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<GoodsInfo> goodsInfos, String message) {
                super.onHttpResponse(isSuccess, goodsInfos, message);
                if (!isSuccess) {
                    showToast(message);
                    return;
                }
                float height = Resources.getSystem().getDisplayMetrics().density*220*(goodsInfos.size()/2+goodsInfos.size()%2);//此处的高度需要动态计算
                LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)height);
                lp.setMargins(20,0,20,20);
                gridView.setLayoutParams(lp);
                adapter.setData(goodsInfos);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        }).doPost();
    }

    private void loadData() {
        showLoadingProgressDialog();
        new GetWalletRequester(userManager.getCurId(), new OnHttpResponseCodeListener<WalletInfo>() {
            @Override
            public void onHttpResponse(boolean isSuccess, WalletInfo walletInfo, String message) {
                super.onHttpResponse(isSuccess, walletInfo, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    cardNum = walletInfo.getCount();
                    beibiNum.setText(walletInfo.getPoint());
                    balanceNum.setText(walletInfo.getBalance());
                    bankNum.setText(walletInfo.getCount());
                } else {
                    showToast(message);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                dismissLoadingProgressDialog();
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
