package com.qckj.dabei.ui.main.homesub.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.ui.home.MoreBusinessActActivity;
import com.qckj.dabei.ui.main.homesub.adapter.HomeBusinessActAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.ScrollGridLayoutManager;
import com.qckj.dabei.view.webview.BrowserActivity;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBusinessActView extends LinearLayout {

    @FindViewById(R.id.view_home_boutique_recommend_recycler_view)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.home_boutique_recommend_view)
    private CommonItemView moreBusinessAct;

    private HomeBusinessActAdapter homeBusinessActAdapter;
    Context context;

    public void setHomeBoutiqueRecommendInfo(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        homeBusinessActAdapter.setHomeBoutiqueRecommendInfos(homeBoutiqueRecommendInfos);
    }

    public HomeBusinessActView(Context context) {
        this(context, null);
    }

    public HomeBusinessActView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeBusinessActView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
        initData();
        initListener();
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_boutique_recommend, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
    }

    private void initData() {
        ScrollGridLayoutManager gridLayoutManager = new ScrollGridLayoutManager(getContext(), 2);
        gridLayoutManager.setScrollEnable(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        homeBusinessActAdapter = new HomeBusinessActAdapter(getContext());
        mRecyclerView.setAdapter(homeBusinessActAdapter);
    }

    private void initListener() {
        moreBusinessAct.setOnClickListener(v -> MoreBusinessActActivity.startActivity(context));
        homeBusinessActAdapter.setOnItemClickListener(new HomeBusinessActAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(HomeBoutiqueRecommendInfo info) {
                BrowserActivity.startActivity(context,info.getUrl(),info.getTitle(),true,info.getIntroduce());
            }
        });
    }
}
