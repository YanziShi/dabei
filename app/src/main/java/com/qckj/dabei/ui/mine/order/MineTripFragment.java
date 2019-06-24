package com.qckj.dabei.ui.mine.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.order.GetMineTripRequester;
import com.qckj.dabei.model.mine.MineTripInfo;
import com.qckj.dabei.ui.mine.order.adapter.MineTripAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;
import com.qckj.dabei.view.webview.BrowserActivity;

import java.util.List;

/**
 * 网车行程
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineTripFragment extends BaseFragment {
    private int curPage = 1;
    public static final int PAGE_SIZE = 10;
    private View rootView;

    @FindViewById(R.id.mine_trip_list)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;
    private MineTripAdapter mineTripAdapter;

    public static MineTripFragment newInstance() {
        return new MineTripFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.mine_trip_list, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initListener();
        return rootView;
    }

    private void initView() {
        mineTripAdapter = new MineTripAdapter(getContext());
        pullRefreshView.setAdapter(mineTripAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
    }

    private void loadData(boolean isRefresh) {
        new GetMineTripRequester(curPage, PAGE_SIZE, userManager.getCurId(), new OnHttpResponseCodeListener<List<MineTripInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<MineTripInfo> mineTripInfos, String message) {
                super.onHttpResponse(isSuccess, mineTripInfos, message);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    ((BaseActivity) getActivity()).showToast(message);
                    return;
                }
                mineTripAdapter.addData(mineTripInfos);
                if (mineTripInfos.size() == 10) {
                    pullRefreshView.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (mineTripInfos.size() == 0) {
                    noRecord.setVisibility(View.VISIBLE);
                    pullRefreshView.setLoadMoreEnable(false);
                } else {
                    noRecord.setVisibility(View.GONE);
                    pullRefreshView.setLoadMoreEnable(false);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                pullRefreshView.setLoadMoreEnable(false);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
            }
        }).doPost();
    }

    private void initListener() {
        mineTripAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<MineTripInfo>() {
            @Override
            public void onAdapterItemClick(int position, MineTripInfo mineTripInfo) {
               //打开网车web
                BrowserActivity.startActivity(getContext(), SystemConfig.carUrl, "大贝网车",mineTripInfo.getTaskType());
            }
        });
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                mineTripAdapter.setDataNull();
                loadData(true);
            }
        });
        pullRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                loadData(false);
            }
        });
        pullRefreshView.startPullRefresh();
    }
}
