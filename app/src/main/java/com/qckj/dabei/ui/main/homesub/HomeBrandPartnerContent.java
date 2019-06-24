package com.qckj.dabei.ui.main.homesub;

import android.content.Context;
import android.view.View;

import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.ui.main.homesub.HomeBaseContent;
import com.qckj.dabei.ui.main.homesub.view.HomeBrandPartnerView;

import java.util.List;

/**
 * 首页品牌合作商
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBrandPartnerContent extends HomeBaseContent<List<HomeBrandPartnerInfo>> {

    private HomeBrandPartnerView homeBrandPartnerView;

    public HomeBrandPartnerContent(List<HomeBrandPartnerInfo> homeBrandPartnerInfos) {
        super(homeBrandPartnerInfos);
    }

    @Override
    public View onCreateSubView(Context context) {
        homeBrandPartnerView = new HomeBrandPartnerView(context);
        onRefreshView(getData());
        return homeBrandPartnerView;
    }

    @Override
    public void onDestroySubView() {

    }

    @Override
    public void onRefreshView(List<HomeBrandPartnerInfo> homeBrandPartnerInfos) {
        homeBrandPartnerView.setHomeBrandPartnerInfos(homeBrandPartnerInfos);
    }
}
