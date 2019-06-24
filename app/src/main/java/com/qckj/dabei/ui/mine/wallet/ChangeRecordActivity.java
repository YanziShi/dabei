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
import com.qckj.dabei.manager.mine.merchant.ChangeGoodsRequester;
import com.qckj.dabei.manager.mine.msg.SystemMessageRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.ChangeRecordsRequester;
import com.qckj.dabei.model.mine.ChangeRecordInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.ui.mine.msg.MineMessageActivity;
import com.qckj.dabei.ui.mine.msg.adapter.SystemMsgAdapter;
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
 * Created by yangzhizhong on 2019/5/24.
 */
public class ChangeRecordActivity extends BaseActivity {
    private int curPage = 1;
    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.list_view)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;
    static public String userId ;

    private RecordAdapter adapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_record);
        ViewInject.inject(this);
        userId = userManager.getCurId();
        initView();
        initListener();
    }

    private void initListener() {
        adapter.setListener(new SimpleBaseAdapter.Listener() {
            @Override
            public void isChange() {
                curPage = 1;
                loadData(true);
            }
        });
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
        new ChangeRecordsRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<ChangeRecordInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<ChangeRecordInfo> changeRecordInfos, String message) {
                super.onHttpResponse(isSuccess, changeRecordInfos, message);
                dismissLoadingProgressDialog();
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    pullRefreshView.setLoadMoreEnable(false);
                    showToast(message);
                    return;
                }
                adapter.addData(changeRecordInfos);
                if (changeRecordInfos.size() == 10) {
                    pullRefreshView.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (changeRecordInfos.size() == 0) {
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
        adapter = new RecordAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
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
