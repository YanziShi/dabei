package com.qckj.dabei.ui.mine.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.msg.AgentInfoRequester;
import com.qckj.dabei.manager.mine.msg.GetMessageRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.BankCardsRequester;
import com.qckj.dabei.model.mine.BankCardInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.ui.mine.msg.MineMessageActivity;
import com.qckj.dabei.ui.mine.msg.SystemMsgActivity;
import com.qckj.dabei.ui.mine.msg.adapter.MessageAdapter;
import com.qckj.dabei.ui.mine.wallet.adapter.BankCardAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class BankCardActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.list_view)
    private PullRefreshView pullRefreshView;

    @Manager
    private UserManager userManager;

    private BankCardAdapter adapter;

    //用来选择银行卡
    boolean isSelecte;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BankCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        ViewInject.inject(this);
        isSelecte = getIntent().getBooleanExtra("isSelecte",false);
        if(isSelecte){
            actionBar.setTitleText("银行卡选择");
        }
        initView();
        initListener();
    }

    private void initListener() {
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    AddBankActivity.startActivity(getActivity());
                }
                return false;
            }
        });

        adapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<BankCardInfo>() {
            @Override
            public void onAdapterItemClick(int position, BankCardInfo bankCardInfo) {
                if(isSelecte){
                    Intent intent = new Intent();
                    intent.putExtra("card_id",bankCardInfo.getBankId());
                    intent.putExtra("card_name",bankCardInfo.getBankName());
                    setResult(RESULT_OK,intent);
                    finish();
                } else UnbindBankActivity.startActivity(BankCardActivity.this,bankCardInfo);
            }
        });
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                loadData();
            }
        });
    }

    private void loadData() {
        pullRefreshView.startPullRefresh();
        new BankCardsRequester( userManager.getCurId(), new OnHttpResponseCodeListener<List<BankCardInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<BankCardInfo> bankCardInfos, String message) {
                super.onHttpResponse(isSuccess, bankCardInfos, message);
                pullRefreshView.stopPullRefresh();
                if (isSuccess) {
                    adapter.setData(bankCardInfos);
                }else {
                    showToast(message);
                }

            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                pullRefreshView.stopPullRefresh();
            }
        }).doPost();
    }

    private void initView() {
        adapter = new BankCardAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);

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
