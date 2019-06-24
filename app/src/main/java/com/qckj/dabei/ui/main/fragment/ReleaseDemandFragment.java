package com.qckj.dabei.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.cache.CacheManager;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.ui.main.adapter.ReleaseProjectAdapter;
import com.qckj.dabei.ui.main.adapter.ReleaseTypeAdapter;
import com.qckj.dabei.ui.release.DemandDescribeActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.List;

/**
 * 发布需求
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class ReleaseDemandFragment extends BaseFragment {

    @FindViewById(R.id.release_type_list_view)
    private ListView mReleaseTypeListView;

    @FindViewById(R.id.release_project_list_view)
    private ListView mReleaseProjectListView;

    @Manager
    private HomeDataManager homeDataManager;
    @Manager
    private CacheManager cacheManager;

    private View rootView;
    private List<HomeFunctionInfo> mHomeFunctionInfos;
    private ReleaseTypeAdapter releaseTypeAdapter;
    private ReleaseProjectAdapter releaseProjectAdapter;
    private int curPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mHomeFunctionInfos = cacheManager.getList(CacheManager.KEY_HOME_FUNCTION_INFO, HomeFunctionInfo.class);
        releaseTypeAdapter = new ReleaseTypeAdapter(getContext());
        releaseProjectAdapter = new ReleaseProjectAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_release_demand, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void initListener() {
        releaseTypeAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeFunctionInfo>() {
            @Override
            public void onAdapterItemClick(int position, HomeFunctionInfo info) {
                curPosition = position;
                releaseTypeAdapter.setCurPosition(position);
                List<HomeFunctionInfo.Category> categoryList = releaseTypeAdapter.getItem(position).getCategoryList();
                releaseProjectAdapter.setData(categoryList);
            }
        });
        releaseProjectAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<HomeFunctionInfo.Category>() {
            @Override
            public void onAdapterItemClick(int position, HomeFunctionInfo.Category category) {
                DemandDescribeActivity.startActivity(getContext(), category.getClassName(),category.getClassId());
            }
        });
    }

    private void initData() {
        homeDataManager.getHomeFunctionInfo(new OnHttpResponseCodeListener<List<HomeFunctionInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<HomeFunctionInfo> homeFunctionInfos, String message) {
                super.onHttpResponse(isSuccess, homeFunctionInfos, message);
                mHomeFunctionInfos = homeFunctionInfos;
                if (isSuccess) {
                    refreshData();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        });
    }

    private void initView() {
        mReleaseTypeListView.setAdapter(releaseTypeAdapter);
        mReleaseProjectListView.setAdapter(releaseProjectAdapter);
        refreshData();
    }

    private void refreshData() {
        if (mHomeFunctionInfos != null && mHomeFunctionInfos.size() > 0) {
            releaseTypeAdapter.setData(mHomeFunctionInfos);
            List<HomeFunctionInfo.Category> categoryList = releaseTypeAdapter.getItem(curPosition).getCategoryList();
            releaseProjectAdapter.setData(categoryList);
        }
    }
}
