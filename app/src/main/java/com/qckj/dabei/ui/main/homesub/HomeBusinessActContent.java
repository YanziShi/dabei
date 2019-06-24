package com.qckj.dabei.ui.main.homesub;

import android.content.Context;
import android.view.View;

import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.ui.main.homesub.view.HomeBusinessActView;

import java.util.List;

/**
 * 首页推荐信息
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBusinessActContent extends HomeBaseContent<List<HomeBoutiqueRecommendInfo>> {

    private HomeBusinessActView homeBusinessActView;

    public HomeBusinessActContent(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        super(homeBoutiqueRecommendInfos);
    }

    @Override
    public View onCreateSubView(Context context) {
        homeBusinessActView = new HomeBusinessActView(context);
        onRefreshView(getData());
        return homeBusinessActView;
    }

    @Override
    public void onDestroySubView() {

    }

    @Override
    public void onRefreshView(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        homeBusinessActView.setHomeBoutiqueRecommendInfo(homeBoutiqueRecommendInfos);
    }
}
