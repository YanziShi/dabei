package com.qckj.dabei.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResultListener;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.HomeBannerInfo;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.model.home.HomeTransactionInfo;
import com.qckj.dabei.model.home.HotMerchantInfo;
import com.qckj.dabei.ui.home.ChangeAddressActivity;
import com.qckj.dabei.ui.home.SearchActivity;
import com.qckj.dabei.ui.home.ErCodeScanActivity;
import com.qckj.dabei.ui.main.adapter.HomeBannerAdapter;
import com.qckj.dabei.ui.main.homesub.HomeBaseContent;
import com.qckj.dabei.ui.main.homesub.HomeBusinessActContent;
import com.qckj.dabei.ui.main.homesub.HomeBrandPartnerContent;
import com.qckj.dabei.ui.main.homesub.HomeFunctionContent;
import com.qckj.dabei.ui.main.homesub.HomeTransactionContent;
import com.qckj.dabei.ui.main.homesub.adapter.HotMerchantAdapter;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.banner.AutoBannerView;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class HomeFragment extends BaseFragment {

    public static final int KEY_HOME_FUNCTION = 1;
    public static final int KEY_HOME_TRANSACTION = 2;
    public static final int KEY_HOME_BRAND_PARTNER = 3;
    public static final int KEY_HOME_BOUTIQUE_RECOMMEND = 4;

    public static final int REQUESTER_SCAN_CODE = 4;

    public static final int PAGE_SIZE = 10;

    private ViewGroup rootView;
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.sub_view_contain)
    private LinearLayout mSubViewContain;

    @FindViewById(R.id.banner_view)
    private AutoBannerView mBannerView;

    @Manager
    private HomeDataManager mHomeDataManager;

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    private HomeBannerAdapter homeBannerAdapter;
    private SparseArray<HomeBaseContent> baseContentSparseArray = new SparseArray<>();
    private ArrayList<HomeBaseContent> baseContentList = new ArrayList<>();
    private HotMerchantAdapter hotMerchantAdapter;

    private int curPage = 1;
    private ImageView mErCodeScan;
    private TextView mCurLocation;
    private View groupLocation;
    double latitude;
    double longitude;
    String city;
    String district;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        longitude = gaoDeLocationManager.getUserLocationInfo().getLongitude();
        latitude  = gaoDeLocationManager.getUserLocationInfo().getLatitude();
        city      = gaoDeLocationManager.getUserLocationInfo().getCity();
        district  = gaoDeLocationManager.getUserLocationInfo().getDistrict();
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            homeBannerAdapter.notifyDataSetChanged();
            return rootView;
        }
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        initPullRefreshView(inflater, rootView);
        ViewInject.inject(this, rootView);
        initContentView();
        initListener();
        return rootView;
    }

    private void initListener() {
        groupLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChangeAddressActivity.startActivity(getActivity(),gaoDeLocationManager.getUserLocationInfo().getProvince(),city,district);
            }
        });
        rootView.findViewById(R.id.layout_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        mErCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ErCodeScanActivity.class);
                startActivityForResult(intent,REQUESTER_SCAN_CODE);
            }
        });
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
                        +"&poi="+ longitude+","+latitude;
                BrowserActivity.startActivity(getContext(),url,false);
            }
        });
    }

    private void initData() {
        homeBannerAdapter = new HomeBannerAdapter(getContext());
        hotMerchantAdapter = new HotMerchantAdapter(getContext());
    }


    private void initPullRefreshView(LayoutInflater inflater, ViewGroup rootView) {
        mErCodeScan = rootView.findViewById(R.id.er_code_scan);
        mCurLocation = rootView.findViewById(R.id.cur_location);
        groupLocation = rootView.findViewById(R.id.group_left_location);
        pullRefreshView = rootView.findViewById(R.id.home_list_view);
        pullRefreshView.setAdapter(hotMerchantAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
        View headView = inflater.inflate(R.layout.fragment_home_head_view, pullRefreshView, false);
        pullRefreshView.addHeaderView(headView);
        pullRefreshView.setOnPullRefreshListener(pullRefreshView -> {
            curPage = 1;
            hotMerchantAdapter.setDataNull();
            // 加载数据
            loadData(isSuccess -> {
                if (isSuccess) {
                    baseContentList.clear();
                    addSubView();
                }
            });
        });

        pullRefreshView.setOnLoadMoreListener(pullRefreshView -> loadMoreHotMerchant());
    }

    private void loadMoreHotMerchant() {
        mHomeDataManager.GetHomeHotMerchantListInfo(PAGE_SIZE, curPage, city,district,
                longitude, latitude,new OnHttpResponseCodeListener<List<HotMerchantInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                if (isSuccess && hotMerchantInfos.size() > 0) {
                    hotMerchantAdapter.addData(hotMerchantInfos);
                    if (hotMerchantInfos.size() == PAGE_SIZE) {
                        pullRefreshView.setLoadMoreEnable(true);
                        curPage++;
                    } else {
                        pullRefreshView.setLoadMoreEnable(false);
                    }
                    pullRefreshView.stopLoadMore();
                }
            }
        });
    }

    private void loadData(OnResultListener onResultListener) {
        boolean[] result = new boolean[]{false, false, false, false, false, false};
        getHomeBanner(onResultListener, result);
        getHomeFunction(onResultListener, result);
        getHomeTransaction(onResultListener, result);
        getHomeBrandPartner(onResultListener, result);
        getBusinessActInfo(onResultListener, result);
        getHotMerchantInfo(onResultListener, result);
    }

    private void getHotMerchantInfo(OnResultListener onResultListener, boolean[] result) {
        mHomeDataManager.GetHomeHotMerchantListInfo(PAGE_SIZE, curPage, city, district, longitude, latitude,
                new OnHttpResponseCodeListener<List<HotMerchantInfo>>() {

            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                pullRefreshView.stopPullRefresh();
                if (isSuccess) {
                    hotMerchantAdapter.setData(hotMerchantInfos);
                    if (hotMerchantInfos.size() == PAGE_SIZE) {
                        pullRefreshView.setLoadMoreEnable(true);
                        curPage++;
                    } else {
                        pullRefreshView.setLoadMoreEnable(false);
                    }
                }else {
                    pullRefreshView.setLoadMoreEnable(false);
                }
                result[5] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                pullRefreshView.setLoadMoreEnable(false);
                pullRefreshView.stopPullRefresh();
                result[5] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    //商家活动
    private void getBusinessActInfo(OnResultListener onResultListener, boolean[] result) {

        mHomeDataManager.GetHomeBoutiqueRecommendInfo(4,1,city,district,
                new OnHttpResponseCodeListener<List<HomeBoutiqueRecommendInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos, String message) {
                super.onHttpResponse(isSuccess, homeBoutiqueRecommendInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_BOUTIQUE_RECOMMEND, new HomeBusinessActContent(homeBoutiqueRecommendInfos));
                }
                result[4] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[4] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    //获取商家合作商，大牌来了
    private void getHomeBrandPartner(OnResultListener onResultListener, boolean[] result) {
        mHomeDataManager.GetHomeBrandPartnerInfo(city,district,
                new OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBrandPartnerInfo> homeBrandPartnerInfos, String message) {
                super.onHttpResponse(isSuccess, homeBrandPartnerInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_BRAND_PARTNER, new HomeBrandPartnerContent(homeBrandPartnerInfos));
                }
                result[3] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[3] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    //交易动态
    private void getHomeTransaction(OnResultListener onResultListener, boolean[] result) {
        mHomeDataManager.getTransactionInfo(new OnHttpResponseCodeListener<List<HomeTransactionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeTransactionInfo> homeTransactionInfos, String message) {
                super.onHttpResponse(isSuccess, homeTransactionInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_TRANSACTION, new HomeTransactionContent(homeTransactionInfos));
                }
                result[2] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[2] = true;
                notifyAllFinish(onResultListener, result);
            }
        });
    }

    //功能列表，广告轮播下面的视图,服务商
    private void getHomeFunction(OnResultListener onResultListener, boolean[] result) {

        mHomeDataManager.getHomeFunctionInfo(new OnHttpResponseCodeListener<List<HomeFunctionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeFunctionInfo> homeFunctionInfos, String message) {
                super.onHttpResponse(isSuccess, homeFunctionInfos, message);
                if (isSuccess) {
                    baseContentSparseArray.put(KEY_HOME_FUNCTION, new HomeFunctionContent(homeFunctionInfos));
                }
                result[1] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[1] = true;
                notifyAllFinish(onResultListener, result);
            }
        });
    }

    private void getHomeBanner(OnResultListener onResultListener, boolean[] result) {

        mHomeDataManager.getHomeBannerInfo(city, district, new OnHttpResponseCodeListener<List<HomeBannerInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBannerInfo> homeBannerInfoList, String message) {
                super.onHttpResponse(isSuccess, homeBannerInfoList, message);
                if (isSuccess) {
                    homeBannerAdapter.changeItems(homeBannerInfoList);
                }
                result[0] = true;
                notifyAllFinish(onResultListener, result);
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                result[0] = true;
                notifyAllFinish(onResultListener, result);
            }
        });

    }

    private void notifyAllFinish(OnResultListener onResultListener, boolean[] result) {
        boolean state = true;
        for (boolean b : result) {
            if (!b) {
                state = false;
                break;
            }
        }
        if (state) {
            onResultListener.onResult(true);
        }
    }

    private void addSubView() {
        HomeBaseContent homeFunctionBaseContent = baseContentSparseArray.get(KEY_HOME_FUNCTION);
        if (homeFunctionBaseContent != null) {
            baseContentList.add(homeFunctionBaseContent);
        }
        HomeBaseContent homeTransactionBaseContent = baseContentSparseArray.get(KEY_HOME_TRANSACTION);
        if (homeTransactionBaseContent != null) {
            baseContentList.add(homeTransactionBaseContent);
        }

        HomeBaseContent homeBrandPartnerBaseContent = baseContentSparseArray.get(KEY_HOME_BRAND_PARTNER);
        if (homeBrandPartnerBaseContent != null) {
            baseContentList.add(homeBrandPartnerBaseContent);
        }

        HomeBaseContent homeBoutiqueRecommendBaseContent = baseContentSparseArray.get(KEY_HOME_BOUTIQUE_RECOMMEND);
        if (homeBoutiqueRecommendBaseContent != null) {
            baseContentList.add(homeBoutiqueRecommendBaseContent);
        }

        mSubViewContain.removeAllViews();
        for (HomeBaseContent baseContent : baseContentList) {
            View topView = new View(getContext());
            topView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dipToPx(getActivity(), 11)));
            View childView = baseContent.onCreateSubView(getContext());
            topView.setBackgroundColor(Color.parseColor("#f7f7f7"));
            mSubViewContain.addView(childView);
        }
    }

    private void initContentView() {
        pullRefreshView.startPullRefresh();
        mBannerView.setWaitMilliSecond(3000);
        mBannerView.setAdapter(homeBannerAdapter);
        mCurLocation.setText(district);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JSONObject jsonObject) {
        if(jsonObject!=null){
            try {
                longitude = jsonObject.getDouble("longitude");
                latitude  = jsonObject.getDouble("latitude");
                city      = jsonObject.getString("city");
                district  = jsonObject.getString("district");
                mCurLocation.setText(district);
                pullRefreshView.startPullRefresh();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK && requestCode == REQUESTER_SCAN_CODE){
            MsgDialog dialog = new MsgDialog(getContext());
            dialog.show("扫描结果",data.getStringExtra("code"),"打开",true);
            dialog.setOnPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    BrowserActivity.startActivity(getContext(),data.getStringExtra("code"),false);
                }
            });
        }
    }
}
