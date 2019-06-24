package com.qckj.dabei.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.ui.home.adapter.CategoryAdapter;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.nearby.GetNearbyNerchantListRequester;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.ui.main.homesub.adapter.HotMerchantAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/9.
 */
public class ServiceBusinessActivity extends BaseActivity{
    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;
    @FindViewById(R.id.tv_category)
    private TextView tvCategory;
    @FindViewById(R.id.list_category)
    private ListView listCategory;
    @FindViewById(R.id.linear_category_type)
    private ViewGroup linearCategoryType;
    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    private HomeFunctionInfo info;
    private int curPage = 1;
    private int PAGE_SIZE = 10;

    private HotMerchantAdapter hotMerchantAdapter;
    private CategoryAdapter categoryAdapter;
    private HomeFunctionInfo.Category category;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;
    int type = 0;
    int categoryCurPosition;

    public static void startActivity(Context context, HomeFunctionInfo info,int categoryCurPosition){
        Intent intent = new Intent(context,ServiceBusinessActivity.class);
        intent.putExtra("info",info);
        intent.putExtra("categoryCurPosition",categoryCurPosition);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_business);
        ViewInject.inject(this);

        info = (HomeFunctionInfo) getIntent().getSerializableExtra("info");
        categoryCurPosition = getIntent().getIntExtra("categoryCurPosition",0);
        category = info.getCategoryList().get(categoryCurPosition);
        actionBar.setTitleText(info.getName());
        tvCategory.setText(category.getClassName());

        categoryAdapter = new CategoryAdapter(this);
        listCategory.setAdapter(categoryAdapter);
        categoryAdapter.setData(info.getCategoryList());
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = info.getCategoryList().get(position);
                tvCategory.setText(category.getClassName());
                listCategory.setVisibility(View.GONE);
                type = 0;
                initView();
            }
        });

        hotMerchantAdapter = new HotMerchantAdapter(this);
        pullRefreshView.setAdapter(hotMerchantAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(pullRefreshView -> {
            curPage = 1;
            hotMerchantAdapter.setDataNull();
            loadData(true);
        });
        pullRefreshView.setOnLoadMoreListener(pullRefreshView -> loadData(false));
        hotMerchantAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HotMerchantInfo>() {
            @Override
            public void onAdapterItemClick(int position, HotMerchantInfo hotMerchantInfo) {
                String isJpsj = "";
                if(hotMerchantInfo.getIsGold()==1) isJpsj = "gold";
                else isJpsj = "general";
                String userId = App.getInstance().getManager(UserManager.class).getCurId();

                String url = SystemConfig.webUrl+"/#/merchant?shopId="+ hotMerchantInfo.getId()
                        +"&type="+ isJpsj
                        + "&userId="+ userId
                        +"&poi="+ gaoDeLocationManager.getUserLocationInfo().getLongitude()+","
                        +gaoDeLocationManager.getUserLocationInfo().getLatitude();
                BrowserActivity.startActivity(getActivity(),url,false);
            }
        });

        initView();
    }

    void initView(){
        for(int i=0; i<linearCategoryType.getChildCount();i++){
            linearCategoryType.getChildAt(i).setSelected(i==type?true:false);
        }
        curPage = 1;
        hotMerchantAdapter.setDataNull();
        pullRefreshView.startPullRefresh();
    }

    private void loadData(boolean ispullRefresh){
        new GetNearbyNerchantListRequester(PAGE_SIZE,curPage,category.getClassId(),
                gaoDeLocationManager.getUserLocationInfo().getCity(),
                gaoDeLocationManager.getUserLocationInfo().getDistrict(),
                gaoDeLocationManager.getUserLocationInfo().getLongitude(),
                gaoDeLocationManager.getUserLocationInfo().getLatitude(),
                type,new OnHttpResponseCodeListener<List<HotMerchantInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                if(ispullRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (isSuccess && hotMerchantInfos.size() > 0) {
                    hotMerchantAdapter.addData(hotMerchantInfos);
                    if(hotMerchantInfos.size()<10) pullRefreshView.setLoadMoreEnable(false);
                    else pullRefreshView.setLoadMoreEnable(true);
                    pullRefreshView.stopLoadMore();
                }else {
                    pullRefreshView.setLoadMoreEnable(false);
                    showToast(message);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                if(ispullRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
            }
        }).doPost();
    }

    @OnClick({R.id.btn_category,R.id.tv_advice,R.id.tv_new,R.id.tv_distance})
     void onViewClick(View v) {
        switch (v.getId()){
            case R.id.btn_category:
                listCategory.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_advice:
                type = 1;
                initView();
                break;
            case R.id.tv_new:
                type = 2;
                initView();
                break;
            case R.id.tv_distance:
                type = 3;
                initView();
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
