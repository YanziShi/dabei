package com.qckj.dabei.ui.mine.resource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.lib.GetDemandLibRequester;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.lib.DemandLibInfo;
import com.qckj.dabei.ui.lib.DemandDetailActivity;
import com.qckj.dabei.ui.lib.adapter.DemandLibTableAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/13.
 */
public class MineResourceActivity extends BaseActivity {

    public static final int PAGE_SIZE = 10;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;
    private UserLocationInfo userLocationInfo;
    private int curPage = 1;
    private DemandLibTableAdapter demandLibTableAdapter;

    @Manager
    private UserManager userManager;
    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MineResourceActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_resource);
        ViewInject.inject(this);
        userLocationInfo = gaoDeLocationManager.getUserLocationInfo();
        demandLibTableAdapter = new DemandLibTableAdapter(this);
        demandLibTableAdapter.setUserManager(userManager);
        pullRefreshView.setAdapter(demandLibTableAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                demandLibTableAdapter.setDataNull();
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

        demandLibTableAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<DemandLibInfo>() {
            @Override
            public void onAdapterItemClick(int position, DemandLibInfo demandLibInfo) {
                Intent intent = new Intent(MineResourceActivity.this, DemandDetailActivity.class);
                intent.putExtra("id", demandLibInfo.getId());
                startActivity(intent);
            }
        });
    }

    private void loadData(boolean isRefresh) {
        new GetDemandLibRequester("", curPage, PAGE_SIZE, "", userLocationInfo.getLongitude() + "", userLocationInfo.getLatitude() + "",
                userLocationInfo.getCity(), userLocationInfo.getDistrict(),"1",userManager.getUserInfo().getId(),
                new OnHttpResponseCodeListener<List<DemandLibInfo>>() {

                    @Override
                    public void onHttpResponse(boolean isSuccess, List<DemandLibInfo> demandLibInfos, String message) {
                        super.onHttpResponse(isSuccess, demandLibInfos, message);
                        if (isRefresh) pullRefreshView.stopPullRefresh();
                        else pullRefreshView.stopLoadMore();
                        if (isSuccess) {
                            demandLibTableAdapter.addData(demandLibInfos);
                            if (demandLibInfos.size() == PAGE_SIZE) {
                                curPage++;
                                pullRefreshView.setLoadMoreEnable(true);
                            } else {
                                pullRefreshView.setLoadMoreEnable(false);
                            }
                        } else {
                            pullRefreshView.setLoadMoreEnable(false);
                            showToast(message);
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