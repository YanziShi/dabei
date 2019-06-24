package com.qckj.dabei.manager.home;

import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.cache.CacheManager;
import com.qckj.dabei.model.home.HomeBannerInfo;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.model.home.HomeTransactionInfo;
import com.qckj.dabei.model.home.HotMerchantInfo;

import java.util.List;

/**
 * 首页数据管理器
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class HomeDataManager extends BaseManager {
    @Override
    public void onManagerCreate(App app) {

    }

    public void getHomeBannerInfo(String city, String district, OnHttpResponseCodeListener<List<HomeBannerInfo>> resultListener) {
        new GetHomeBannerInfoRequester(city, district, new OnHttpResponseCodeListener<List<HomeBannerInfo>>() {

            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBannerInfo> homeBannerInfoList, String message) {
                super.onHttpResponse(isSuccess, homeBannerInfoList, message);
                resultListener.onHttpResponse(isSuccess, homeBannerInfoList, message);
            }
        }).doPost();
    }

    public void getHomeFunctionInfo(OnHttpResponseCodeListener<List<HomeFunctionInfo>> resultListener) {
        new GetHomeFunctionInfoRequester(new OnHttpResponseCodeListener<List<HomeFunctionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeFunctionInfo> homeFunctionInfos, String message) {
                super.onHttpResponse(isSuccess, homeFunctionInfos, message);
                if (isSuccess) {
                    CacheManager cacheManager = getManager(CacheManager.class);
                    cacheManager.putList(CacheManager.KEY_HOME_FUNCTION_INFO, homeFunctionInfos);
                }
                resultListener.onHttpResponse(isSuccess, homeFunctionInfos, message);
            }
        }).doPost();
    }

    public void getTransactionInfo(OnHttpResponseCodeListener<List<HomeTransactionInfo>> resultListener) {
        new GetTransactionInfoRequester(new OnHttpResponseCodeListener<List<HomeTransactionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeTransactionInfo> homeTransactionInfos, String message) {
                super.onHttpResponse(isSuccess, homeTransactionInfos, message);
                resultListener.onHttpResponse(isSuccess, homeTransactionInfos, message);
            }
        }).doPost();
    }

    public void GetHomeBrandPartnerInfo(String addrestwo,
                                        String addresthree,
                                        OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>> resultListener) {
        new GetHomeBrandPartnerRequester(addrestwo,addresthree,new OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>>() {

            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBrandPartnerInfo> homeBrandPartnerInfos, String message) {
                super.onHttpResponse(isSuccess, homeBrandPartnerInfos, message);
                resultListener.onHttpResponse(isSuccess, homeBrandPartnerInfos, message);
            }
        }).doPost();

    }

    public void GetHomeBoutiqueRecommendInfo(int rows,int page, String city, String district,OnHttpResponseCodeListener<List<HomeBoutiqueRecommendInfo>> resultListener) {
        new GetBusinessActRequester(rows,page,city,district,
                new OnHttpResponseCodeListener<List<HomeBoutiqueRecommendInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos, String message) {
                super.onHttpResponse(isSuccess, homeBoutiqueRecommendInfos, message);
                resultListener.onHttpResponse(isSuccess, homeBoutiqueRecommendInfos, message);
            }
        }).doPost();

    }

    public void GetHomeHotMerchantListInfo(int pageSize, int page, String city, String district, double longitude, double latitude,
                                           OnHttpResponseCodeListener<List<HotMerchantInfo>> resultListener) {
        new GetHomeHotMerchantListRequester(pageSize, page, city, district, longitude, latitude,
                new OnHttpResponseCodeListener<List<HotMerchantInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HotMerchantInfo> hotMerchantInfos, String message) {
                super.onHttpResponse(isSuccess, hotMerchantInfos, message);
                resultListener.onHttpResponse(isSuccess, hotMerchantInfos, message);
            }
        }).doPost();
    }


}
