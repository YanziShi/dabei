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
import com.qckj.dabei.ui.mine.order.adapter.MineReleaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.listview.OnLoadMoreListener;
import com.qckj.dabei.view.listview.OnPullRefreshListener;
import com.qckj.dabei.view.listview.PullRefreshView;

import java.util.List;

/**
 * 我的发布
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineReleaseFragment extends BaseFragment {

    private int curPage = 1;
    public static final int PAGE_SIZE = 10;
    private View rootView;

    @FindViewById(R.id.mine_release_list)
    private PullRefreshView pullRefreshView;

    @FindViewById(R.id.no_record)
    private TextView noRecord;

    @Manager
    private UserManager userManager;

    private MineReleaseAdapter mineReleaseAdapter;

    public static MineReleaseFragment newInstance() {
        return new MineReleaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.mine_release_list, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initListener();
        return rootView;
    }

    private void initListener() {
        pullRefreshView.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                curPage = 1;
                mineReleaseAdapter.setDataNull();
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
        mineReleaseAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<MineReleaseInfo>() {
            @Override
            public void onAdapterItemClick(int position, MineReleaseInfo mineReleaseInfo) {
                Intent intent = new Intent(getContext(),ReleaseDetailActivity.class);
                intent.putExtra("data",mineReleaseInfo);
                startActivity(intent);
            }
        });
    }

    private void loadData(boolean isRefresh) {
        new GetMineOrderRequester(curPage, PAGE_SIZE, userManager.getCurId(), 1, new OnHttpResponseCodeListener<List<MineReleaseInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<MineReleaseInfo> mineReleaseInfos, String message) {
                super.onHttpResponse(isSuccess, mineReleaseInfos, message);
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                if (!isSuccess) {
                    ((BaseActivity) getActivity()).showToast(message);
                    return;
                }
                mineReleaseAdapter.addData(mineReleaseInfos);
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
                if (isRefresh) pullRefreshView.stopPullRefresh();
                else pullRefreshView.stopLoadMore();
                pullRefreshView.setLoadMoreEnable(false);
            }
        }).doPost();


    }

    private void initView() {
        mineReleaseAdapter = new MineReleaseAdapter(getContext());
        pullRefreshView.setAdapter(mineReleaseAdapter);
        pullRefreshView.setPullRefreshEnable(true);
        pullRefreshView.setLoadMoreEnable(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            pullRefreshView.startPullRefresh();
        }
    }
}
