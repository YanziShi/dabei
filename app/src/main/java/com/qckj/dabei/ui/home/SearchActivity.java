package com.qckj.dabei.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.ui.home.adapter.SearchAdapter;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.SearchRequester;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.SearchAllInfo;
import com.qckj.dabei.ui.lib.DemandDetailActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class SearchActivity extends BaseActivity {
    @FindViewById(R.id.edit_search)
    private EditText editSearch;
    @FindViewById(R.id.all_item)
    private TextView allItem;
    @FindViewById(R.id.merchants_item)
    private TextView merchantsItem;
    @FindViewById(R.id.need_item)
    private TextView needItem;
    @FindViewById(R.id.service_item)
    private TextView serviceItem;
    @FindViewById(R.id.list_view)
    private PullRefreshView pullRefreshView;

    private SearchAdapter adapter;
    private int curPage = 1;
    private int PAGE_SIZE = 10;
    int type = 0;
    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ViewInject.inject(this);
        adapter = new SearchAdapter(this);
        pullRefreshView.setAdapter(adapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(pullRefreshView -> {
            curPage = 1;
            adapter.setDataNull();
            loadData(true);
        });
        pullRefreshView.setOnLoadMoreListener(pullRefreshView -> loadData(false));
        adapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<SearchAllInfo>() {
            @Override
            public void onAdapterItemClick(int position, SearchAllInfo searchAllInfo) {
                if (searchAllInfo.getType().equals("1")) {
                    String isJpsj = "";
                    if(searchAllInfo.getIsjpsj()==1) isJpsj = "gold";
                    else isJpsj = "general";
                    String userId = App.getInstance().getManager(UserManager.class).getCurId();
                    String url = SystemConfig.webUrl+ "/#/merchant?shopId="+ searchAllInfo.getSid()
                            +"&type="+ isJpsj
                            + "&userId="+ userId
                            +"&poi="+ gaoDeLocationManager.getUserLocationInfo().getLongitude()+","
                            +gaoDeLocationManager.getUserLocationInfo().getLatitude();
                    BrowserActivity.startActivity(SearchActivity.this,url,false);
                } else if (searchAllInfo.getType().equals("2")) {
                    Intent intent = new Intent(SearchActivity.this, DemandDetailActivity.class);
                    intent.putExtra("id", searchAllInfo.getSid());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchActivity.this, ServiceDetailActivity.class);
                    intent.putExtra("id",searchAllInfo.getSid());
                    startActivity(intent);
                }
            }
        });
        initView();
    }

    private void initView() {
        switch (type) {
            case 0:
                allItem.setSelected(true);
                merchantsItem.setSelected(false);
                needItem.setSelected(false);
                serviceItem.setSelected(false);
                break;
            case 1:
                allItem.setSelected(false);
                merchantsItem.setSelected(true);
                needItem.setSelected(false);
                serviceItem.setSelected(false);
                break;
            case 2:
                allItem.setSelected(false);
                merchantsItem.setSelected(false);
                needItem.setSelected(true);
                serviceItem.setSelected(false);
                break;
            case 3:
                allItem.setSelected(false);
                merchantsItem.setSelected(false);
                needItem.setSelected(false);
                serviceItem.setSelected(true);
                break;
        }
        pullRefreshView.startPullRefresh();
    }

    private void loadData(boolean ispullRefresh){
        new SearchRequester(editSearch.getText().toString(),type,curPage,PAGE_SIZE,
                gaoDeLocationManager.getUserLocationInfo().getLongitude(),
                gaoDeLocationManager.getUserLocationInfo().getLatitude(),
                new OnHttpResponseCodeListener<List<SearchAllInfo>>(){

                    @Override
                    public void onHttpResponse(boolean isSuccess, List<SearchAllInfo> searchAllBeans, String message) {
                        super.onHttpResponse(isSuccess, searchAllBeans, message);
                        if(ispullRefresh)pullRefreshView.stopPullRefresh();
                        else pullRefreshView.stopLoadMore();
                        if (isSuccess && searchAllBeans != null) {
                            adapter.addData(searchAllBeans);
                            if(searchAllBeans.size()<10)pullRefreshView.setLoadMoreEnable(false);
                            else pullRefreshView.setLoadMoreEnable(true);
                            curPage++;
                        }else {
                            pullRefreshView.setLoadMoreEnable(false);
                            showToast(message);
                        }
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                        pullRefreshView.setLoadMoreEnable(false);
                        if(ispullRefresh)pullRefreshView.stopPullRefresh();
                        else pullRefreshView.stopLoadMore();
                    }
                }).doPost();
    }

    @OnClick({R.id.all_item,R.id.merchants_item,R.id.need_item,R.id.service_item,R.id.btn_search})
    void onViewClick(View v) {
        switch (v.getId()){
            case R.id.all_item:
                type = 0;
                initView();
                break;
            case R.id.merchants_item:
                type = 1;
                initView();
                break;
            case R.id.need_item:
                type = 2;
                initView();
                break;
            case R.id.service_item:
                type = 3;
                initView();
                break;
            case R.id.btn_search:
                pullRefreshView.startPullRefresh();
                break;
        }
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
