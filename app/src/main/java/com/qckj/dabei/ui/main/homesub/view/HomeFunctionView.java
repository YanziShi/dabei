package com.qckj.dabei.ui.main.homesub.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.ui.home.MoreCategoryActivity;
import com.qckj.dabei.ui.home.ServiceBusinessActivity;
import com.qckj.dabei.ui.main.homesub.adapter.HomeFunctionAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ScrollGridLayoutManager;

import java.util.List;

/**
 * 首页功能视图的view
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeFunctionView extends LinearLayout {

    @FindViewById(R.id.view_home_function_recycler_view)
    private RecyclerView mRecyclerView;
    private HomeFunctionAdapter homeFunctionAdapter;
    private Context context;

    public void setHomeFunctionInfos(List<HomeFunctionInfo> homeFunctionInfos) {
        HomeFunctionInfo info = new HomeFunctionInfo();
        info.setName("更多");
        homeFunctionInfos.add(info);
        homeFunctionAdapter.setHomeFunctionInfos(homeFunctionInfos);
    }

    public HomeFunctionView(Context context) {
        this(context, null);
    }

    public HomeFunctionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeFunctionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
        initData();
        initListener();
    }

    private void initListener() {

        homeFunctionAdapter.setOnHomeFunctionClickListener(new HomeFunctionAdapter.OnHomeFunctionClickListener() {
            @Override
            public void onFunctionClick(HomeFunctionInfo info) {
                if(info.getImgUrl()!=null){
                    ServiceBusinessActivity.startActivity(context,info,0);
                }else{
                    Intent intent = new Intent(context, MoreCategoryActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        ScrollGridLayoutManager gridLayoutManager = new ScrollGridLayoutManager(getContext(), 5);
        gridLayoutManager.setScrollEnable(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        homeFunctionAdapter = new HomeFunctionAdapter(getContext());
        mRecyclerView.setAdapter(homeFunctionAdapter);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_function, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
    }
}
