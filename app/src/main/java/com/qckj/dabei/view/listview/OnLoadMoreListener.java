package com.qckj.dabei.view.listview;

/**
 * 加载更多
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public interface OnLoadMoreListener {
    /**
     * 产生加载更多事件的回调
     *
     * @param pullRefreshView pullRefreshView
     */
    void onLoadMore(PullRefreshView pullRefreshView);

}
