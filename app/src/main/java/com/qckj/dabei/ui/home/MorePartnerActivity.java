package com.qckj.dabei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.ui.home.adapter.MorePartnerAdapter;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.GetMoreBrandPartnerRequester;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 更多合作商
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class MorePartnerActivity extends BaseActivity {

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;

    MorePartnerAdapter adapter;
    private int curPage = 1;
    private int PAGE_SIZE = 10;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MorePartnerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_partner);
        ViewInject.inject(this);
        adapter = new MorePartnerAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                adapter.setDataNull();
                loadData();
            }
        });
        pullRefreshView.setOnLoadMoreListener(pullRefreshView -> loadData());
        adapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeBrandPartnerInfo>() {
            @Override
            public void onAdapterItemClick(int position, HomeBrandPartnerInfo data) {
                UserManager userManager = App.getInstance().getManager(UserManager.class);
                if (position==0 ) {
                    if(userManager.isLogin())BrowserActivity.startActivity(getActivity(), SystemConfig.carUrl, data.getName());
                    else LoginActivity.startActivity(getActivity());
                }else BrowserActivity.startActivity(getActivity(), data.getUrl(), data.getName());
            }
        });
        loadData();

    }

    private void loadData(){
        curPage = 1;
        new GetMoreBrandPartnerRequester(PAGE_SIZE,curPage,
                gaoDeLocationManager.getUserLocationInfo().getCity(),
                gaoDeLocationManager.getUserLocationInfo().getDistrict(),
                new OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, List<HomeBrandPartnerInfo> homeBrandPartnerInfos, String message) {
                        super.onHttpResponse(isSuccess, homeBrandPartnerInfos, message);
                        pullRefreshView.stopPullRefresh();
                        if (isSuccess && homeBrandPartnerInfos.size() > 0) {
                            adapter.addData(homeBrandPartnerInfos);
                            if (homeBrandPartnerInfos.size() == PAGE_SIZE) {
                                pullRefreshView.setLoadMoreEnable(true);
                                curPage++;
                            } else {
                                pullRefreshView.setLoadMoreEnable(false);
                            }
                        }else {
                            pullRefreshView.setLoadMoreEnable(false);
                            showToast(message);
                        }
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                        pullRefreshView.stopPullRefresh();
                        pullRefreshView.setLoadMoreEnable(false);
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
