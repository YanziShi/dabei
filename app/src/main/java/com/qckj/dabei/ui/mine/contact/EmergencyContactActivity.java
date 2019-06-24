package com.qckj.dabei.ui.mine.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.contact.EmergencyListRequester;
import com.qckj.dabei.manager.mine.msg.SystemMessageRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.contact.ContactInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 紧急联系人
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class EmergencyContactActivity extends BaseActivity {

    @FindViewById(R.id.action_bar)
    ActionBar actionBar;
    @FindViewById(R.id.emergency_contact_list)
    private PullRefreshView pullRefreshView;
    @FindViewById(R.id.no_record)
    private TextView noRecord;
    private EmergencyContactAdapter mEmergencyContactAdapter;
    @Manager
    private UserManager userManager;
    private int curPage = 1;
    public static final int PAGE_SIZE = 15;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EmergencyContactActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);
        ViewInject.inject(this);
        init();
    }

    private void loadData(boolean isRefresh) {
        new EmergencyListRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<ContactInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<ContactInfo> contactInfos, String message) {
                super.onHttpResponse(isSuccess, contactInfos, message);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    pullRefreshView.setLoadMoreEnable(false);
                    showToast(message);
                    return;
                }
                mEmergencyContactAdapter.addData(contactInfos);
                if (contactInfos.size() == PAGE_SIZE) {
                    pullRefreshView.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (contactInfos.size() == 0) {
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

    private void init() {
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_TEXT_RIGHT){
                    EmergencyContactAddActivity.startActivity(getActivity());
                }
                return false;
            }
        });
        mEmergencyContactAdapter = new EmergencyContactAdapter(this);
        pullRefreshView.setAdapter(mEmergencyContactAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                mEmergencyContactAdapter.setDataNull();
                loadData(true);
            }
        });
        pullRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullRefreshView.startPullRefresh();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

