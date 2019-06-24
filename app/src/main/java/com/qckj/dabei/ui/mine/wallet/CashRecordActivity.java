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
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.CashRecordRequester;
import com.qckj.dabei.manager.mine.wallet.ChangeRecordsRequester;
import com.qckj.dabei.model.mine.CashRecordInfo;
import com.qckj.dabei.model.mine.ChangeRecordInfo;
import com.qckj.dabei.ui.mine.msg.MineMessageActivity;
import com.qckj.dabei.ui.mine.wallet.adapter.CashRecordAdapter;
import com.qckj.dabei.ui.mine.wallet.adapter.RecordAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/28.
 */
public class CashRecordActivity extends BaseActivity {
    private int curPage = 1;
    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.list_view)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;
    static public String userId ;

    private CashRecordAdapter adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CashRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_record);
        ViewInject.inject(this);
        userId = userManager.getCurId();
        initView();
        initListener();
    }

    private void initListener() {
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                adapter.setDataNull();
                loadData(true);
            }
        });
        pullRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
        pullRefreshView.startPullRefresh();
    }

    private void loadData(boolean isRefresh) {
        showLoadingProgressDialog();
        new CashRecordRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<CashRecordInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<CashRecordInfo> cashRecordInfos, String message) {
                super.onHttpResponse(isSuccess, cashRecordInfos, message);
                dismissLoadingProgressDialog();
                pullRefreshView.setLoadMoreEnable(false);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    pullRefreshView.setLoadMoreEnable(false);
                    showToast(message);
                    return;
                }
                adapter.addData(cashRecordInfos);
                if (cashRecordInfos.size() == 10) {
                    pullRefreshView.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (cashRecordInfos.size() == 0) {
                    noRecord.setVisibility(View.VISIBLE);
                    pullRefreshView.setLoadMoreEnable(false);
                } else {
                    noRecord.setVisibility(View.GONE);
                    pullRefreshView.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                pullRefreshView.setLoadMoreEnable(false);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
            }
        }).doPost();
    }

    private void initView() {
        adapter = new CashRecordAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(true);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
