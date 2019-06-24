package com.qckj.dabei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.ui.home.adapter.MoreBusinessActAdapter;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.GetBusinessActRequester;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/9.
 */
public class MoreBusinessActActivity extends BaseActivity {

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;

    MoreBusinessActAdapter adapter;
    private int curPage = 1;
    private int PAGE_SIZE = 10;
    List<HomeBoutiqueRecommendInfo> datas;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MoreBusinessActActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_business_act);
        ViewInject.inject(this);
        adapter = new MoreBusinessActAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setOnPullRefreshListener(pullRefreshView -> {
            curPage=1;
            adapter.setDataNull();
            loadData(true);
        });
        pullRefreshView.setLoadMoreEnable(true);
        pullRefreshView.setOnLoadMoreListener(pullRefreshView -> loadData(false));
        adapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeBoutiqueRecommendInfo>() {
            @Override
            public void onAdapterItemClick(int position, HomeBoutiqueRecommendInfo data) {
                BrowserActivity.startActivity(getActivity(),data.getUrl(),data.getTitle(),true,data.getIntroduce());
            }
        });

        pullRefreshView.startPullRefresh();
    }

    private void loadData(boolean isRefresh){
        new GetBusinessActRequester(PAGE_SIZE,curPage,gaoDeLocationManager.getUserLocationInfo().getCity(),gaoDeLocationManager.getUserLocationInfo().getDistrict(),
                new OnHttpResponseCodeListener<List<HomeBoutiqueRecommendInfo>>() {
                    @Override
                    public void onHttpResponse(boolean isSuccess, List<HomeBoutiqueRecommendInfo> homeBrandPartnerInfos, String message) {
                        super.onHttpResponse(isSuccess, homeBrandPartnerInfos, message);
                        if(isRefresh)pullRefreshView.stopPullRefresh();
                        else pullRefreshView.stopLoadMore();
                        if (isSuccess && homeBrandPartnerInfos.size() > 0) {
                            datas= homeBrandPartnerInfos;
                            adapter.setData(homeBrandPartnerInfos);
                            if (homeBrandPartnerInfos.size() == PAGE_SIZE) {
                                pullRefreshView.setLoadMoreEnable(true);
                                curPage++;
                            } else {
                                pullRefreshView.setLoadMoreEnable(false);
                            }
                        }else {
                            showToast(message);
                            pullRefreshView.setLoadMoreEnable(false);
                        }
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                        pullRefreshView.setLoadMoreEnable(false);
                        if(isRefresh)pullRefreshView.stopPullRefresh();
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
