package com.qckj.dabei.ui.main.homesub;

import android.content.Context;
import android.view.View;

import com.qckj.dabei.model.home.HomeTransactionInfo;
import com.qckj.dabei.ui.main.homesub.view.HomeTransactionView;

import java.util.List;

/**
 * 交易信息滚动view
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeTransactionContent extends HomeBaseContent<List<HomeTransactionInfo>> {

    private HomeTransactionView homeTransactionView;

    public HomeTransactionContent(List<HomeTransactionInfo> homeTransactionInfos) {
        super(homeTransactionInfos);
    }

    @Override
    public View onCreateSubView(Context context) {
        homeTransactionView = new HomeTransactionView(context);
        onRefreshView(getData());
        return homeTransactionView;
    }

    @Override
    public void onDestroySubView() {

    }

    @Override
    public void onRefreshView(List<HomeTransactionInfo> homeTransactionInfos) {
            homeTransactionView.setRollDatas(homeTransactionInfos);
    }
}
