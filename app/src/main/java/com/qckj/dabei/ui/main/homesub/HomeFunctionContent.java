package com.qckj.dabei.ui.main.homesub;

import android.content.Context;
import android.view.View;

import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.ui.main.homesub.HomeBaseContent;
import com.qckj.dabei.ui.main.homesub.view.HomeFunctionView;

import java.util.List;

/**
 * 首页功能模块
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeFunctionContent extends HomeBaseContent<List<HomeFunctionInfo>> {

    private HomeFunctionView homeFunctionView;

    public HomeFunctionContent(List<HomeFunctionInfo> homeFunctionInfos) {
        super(homeFunctionInfos);
    }

    @Override
    public View onCreateSubView(Context context) {
        homeFunctionView = new HomeFunctionView(context);
        onRefreshView(getData());
        return homeFunctionView;
    }

    @Override
    public void onDestroySubView() {

    }

    @Override
    public void onRefreshView(List<HomeFunctionInfo> homeFunctionInfos) {
        homeFunctionView.setHomeFunctionInfos(homeFunctionInfos.subList(0, 9));
    }
}
