package com.qckj.dabei.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.ui.home.SelectStreetActivity;
import com.qckj.dabei.ui.home.adapter.CategoryAdapter;
import com.qckj.dabei.ui.home.adapter.ServiceBusinessAdapter;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.home.BusinessPageRequester;
import com.qckj.dabei.manager.cache.CacheManager;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.model.nearby.ServiceInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 附近
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class ServiceBusinessFragment extends BaseFragment{
    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.pull_list_view)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.tv_category)
    private TextView tvCategory;

    @FindViewById(R.id.tv_busi_type)
    private TextView tvBusiType;

    @FindViewById(R.id.tv_sort)
    private TextView tvSort;

    @FindViewById(R.id.list_view)
    private ListView listView;

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;
    UserLocationInfo userLocationInfo;

    @Manager
    private HomeDataManager homeDataManager;

    @Manager
    private CacheManager cacheManager;
    private List<HomeFunctionInfo> infos;
    private List<HomeFunctionInfo.Category> infosCat;    //分类，餐饮、住宿等
    private List<HomeFunctionInfo.Category> infosType;   //类型，全部商家、金牌商家和普通商家
    private List<HomeFunctionInfo.Category> infosSort;    //排序，综合排序、距离最近、人气最高、新店开张
    private int tag;   //标识当前选的什么数据
    private View rootView;
    private ServiceBusinessAdapter hotMerchantAdapter;

    private int curPage = 1;
    private int PAGE_SIZE = 10;
    private CategoryAdapter categoryAdapter;

    private String classId = "";
    private String type = "";
    private String sort = "";
    private String F_C_ID = "";
    protected static final int REQUEST_CODE_CIRCLE = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        infos = cacheManager.getList(CacheManager.KEY_HOME_FUNCTION_INFO, HomeFunctionInfo.class);
        userLocationInfo = gaoDeLocationManager.getUserLocationInfo();

        infosCat = new ArrayList<HomeFunctionInfo.Category>();
        HomeFunctionInfo.Category info0 = new HomeFunctionInfo.Category();
        info0.setClassId("");
        info0.setClassName("全部");
        infosCat.add(info0);
        for(HomeFunctionInfo info:infos){
            HomeFunctionInfo.Category data = new HomeFunctionInfo.Category();
            data.setClassId(info.getId());
            data.setClassName(info.getName());
            infosCat.add(data);
        }
        infosType = new ArrayList<HomeFunctionInfo.Category>();
        HomeFunctionInfo.Category type0 = new HomeFunctionInfo.Category();
        type0.setClassId("0");
        type0.setClassName("全部商家");
        infosType.add(type0);

        HomeFunctionInfo.Category type1 = new HomeFunctionInfo.Category();
        type1.setClassId("1");
        type1.setClassName("金牌商家");
        infosType.add(type1);

        HomeFunctionInfo.Category type2 = new HomeFunctionInfo.Category();
        type2.setClassId("2");
        type2.setClassName("普通商家");
        infosType.add(type2);

        infosSort = new ArrayList<HomeFunctionInfo.Category>();
        HomeFunctionInfo.Category sort0 = new HomeFunctionInfo.Category();
        sort0.setClassId("0");
        sort0.setClassName("综合排序");
        infosSort.add(sort0);

        HomeFunctionInfo.Category sort1 = new HomeFunctionInfo.Category();
        sort1.setClassId("1");
        sort1.setClassName("距离最近");
        infosSort.add(sort1);

        HomeFunctionInfo.Category sort2 = new HomeFunctionInfo.Category();
        sort2.setClassId("2");
        sort2.setClassName("人气最高");
        infosSort.add(sort2);

        HomeFunctionInfo.Category sort3 = new HomeFunctionInfo.Category();
        sort3.setClassId("3");
        sort3.setClassName("新店开张");
        infosSort.add(sort3);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_busi_service, container, false);
        ViewInject.inject(this, rootView);

        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if(function == ActionBar.FUNCTION_BUTTON_RIGHT){
                    Intent intent = new Intent(getActivity(), SelectStreetActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_CIRCLE);
                }
                return false;
            }
        });

        categoryAdapter = new CategoryAdapter(getContext());
        listView.setAdapter(categoryAdapter);
        categoryAdapter.setData(infosCat);
        categoryAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tag == 0){
                    classId = infosCat.get(position).getClassId();
                    tvCategory.setText(infosCat.get(position).getClassName());
                }else if(tag == 1){
                    type = infosType.get(position).getClassId();
                    tvBusiType.setText(infosType.get(position).getClassName());
                }else if(tag ==2){
                    sort = infosSort.get(position).getClassId();
                    tvSort.setText(infosSort.get(position).getClassName());
                }

                listView.setVisibility(View.GONE);
                pullRefreshView.startPullRefresh();
            }
        });

        hotMerchantAdapter = new ServiceBusinessAdapter(getContext());
        pullRefreshView.setAdapter(hotMerchantAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        pullRefreshView.setOnPullRefreshListener(pullRefreshView -> {
            curPage = 1;
            hotMerchantAdapter.setDataNull();
            loadData(true);
        });
        pullRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
        pullRefreshView.startPullRefresh();

        hotMerchantAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<ServiceInfo>() {
            @Override
            public void onAdapterItemClick(int position, ServiceInfo hotMerchantInfo) {
                String isJpsj = "";
                if(hotMerchantInfo.getIsGold()==1) isJpsj = "gold";
                else isJpsj = "general";
                String userId = App.getInstance().getManager(UserManager.class).getCurId();

                String url = SystemConfig.webUrl+"/#/merchant?shopId="+ hotMerchantInfo.getId()
                        +"&type="+ isJpsj
                        + "&userId="+ userId
                        +"&poi="+ userLocationInfo.getLongitude()+","+userLocationInfo.getLatitude();
                BrowserActivity.startActivity(getContext(),url,false);
            }
        });

        return rootView;
    }

    private void loadData(boolean ispullRefresh){
        new BusinessPageRequester(PAGE_SIZE,curPage,
                userLocationInfo.getDistrict(),
                userLocationInfo.getLongitude(),
                userLocationInfo.getLatitude(),
                classId,
                type,
                sort,
                F_C_ID,
                new OnHttpResponseCodeListener<List<ServiceInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<ServiceInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                if(ispullRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (isSuccess && hotMerchantInfos.size() > 0) {
                    hotMerchantAdapter.addData(hotMerchantInfos);
                    if (hotMerchantInfos.size() == PAGE_SIZE) {
                        pullRefreshView.setLoadMoreEnable(true);
                        curPage++;
                    } else {
                        pullRefreshView.setLoadMoreEnable(false);
                    }
                }else {
                    pullRefreshView.setLoadMoreEnable(false);
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                pullRefreshView.setLoadMoreEnable(false);
                if(ispullRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();

            }
        }).doPost();
    }

    @OnClick({R.id.btn_category,R.id.btn_busi_type,R.id.btn_sort})
    void onViewClick(View v) {
        switch (v.getId()){

            case R.id.btn_category:
                listView.setVisibility(View.VISIBLE);
                tag = 0;
                categoryAdapter.setData(infosCat);
                break;
            case R.id.btn_busi_type:
                listView.setVisibility(View.VISIBLE);
                tag = 1;
                categoryAdapter.setData(infosType);
                break;
            case R.id.btn_sort:
                listView.setVisibility(View.VISIBLE);
                tag = 2;
                categoryAdapter.setData(infosSort);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode){
                case REQUEST_CODE_CIRCLE:
                    F_C_ID = data.getStringExtra("F_C_ID");
                    pullRefreshView.startPullRefresh();
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listView.setVisibility(View.GONE);
    }
}
