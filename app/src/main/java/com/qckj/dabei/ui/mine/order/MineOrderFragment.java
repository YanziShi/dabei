package com.qckj.dabei.ui.mine.order;

import android.content.Intent;
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
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.order.GetMineOrderRequester;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.ui.lib.DemandDetailActivity;
import com.qckj.dabei.ui.mine.order.adapter.MineOrderAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.List;

/**
 * 我的接单
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineOrderFragment extends BaseFragment {

    private int curPage = 1;
    public static final int PAGE_SIZE = 10;
    private View rootView;

    @FindViewById(R.id.mine_order_list)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;
    private MineOrderAdapter mineOrderAdapter;

    public static MineOrderFragment newInstance() {
        return new MineOrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.mine_order_list, container, false);
        ViewInject.inject(this, rootView);
        initView();
        loadData(true);
        initListener();
        return rootView;
    }

    private void initListener() {
        mineOrderAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<MineReleaseInfo>() {
            @Override
            public void onAdapterItemClick(int position, MineReleaseInfo mineReleaseInfo) {
                Intent intent = new Intent(getContext(),DemandDetailActivity.class);
                intent.putExtra("id",mineReleaseInfo.getId());
                intent.putExtra("hideServer",true);
                startActivity(intent);
            }
        });
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                mineOrderAdapter.setDataNull();
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

    private void loadData(boolean isRefresh) {
        new GetMineOrderRequester(curPage, PAGE_SIZE, userManager.getCurId(), 2, new OnHttpResponseCodeListener<List<MineReleaseInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<MineReleaseInfo> mineReleaseInfos, String message) {
                super.onHttpResponse(isSuccess, mineReleaseInfos, message);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    ((BaseActivity) getActivity()).showToast(message);
                    return;
                }
                mineOrderAdapter.addData(mineReleaseInfos);
                if (mineReleaseInfos.size() == 10) {
                    pullRefreshView.setLoadMoreEnable(true);
                    noRecord.setVisibility(View.GONE);
                    curPage++;
                } else if (mineReleaseInfos.size() == 0) {
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

    private void initView() {
        mineOrderAdapter = new MineOrderAdapter(getContext());
        pullRefreshView.setAdapter(mineOrderAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
    }
}
