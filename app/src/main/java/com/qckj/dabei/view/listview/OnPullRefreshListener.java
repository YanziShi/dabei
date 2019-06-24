package com.qckj.dabei.view.listview;


/**
 * 下拉的监听接口
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public interface OnPullRefreshListener {
    /**
     * 下拉刷新时调用
     *
     * @param pullRefreshView pullRefreshView
     */
    void onPullDownRefresh(PullRefreshView pullRefreshView);
}
