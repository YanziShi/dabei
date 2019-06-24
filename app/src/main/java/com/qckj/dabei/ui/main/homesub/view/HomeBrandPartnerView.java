package com.qckj.dabei.ui.main.homesub.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.ui.home.MorePartnerActivity;
import com.qckj.dabei.ui.main.homesub.adapter.HomeBrandPartnerAdapter;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.ScrollGridLayoutManager;
import com.qckj.dabei.view.webview.BrowserActivity;

import java.util.List;

/**
 * 首页品牌合作商
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBrandPartnerView extends LinearLayout {

    @FindViewById(R.id.home_brand_partner_view)
    private CommonItemView morePartnerView;

    @FindViewById(R.id.view_home_brand_partner_recycler_view)
    private RecyclerView mRecyclerView;
    private HomeBrandPartnerAdapter homeBrandPartnerAdapter;

    public void setHomeBrandPartnerInfos(List<HomeBrandPartnerInfo> homeBradPartnnerInfos) {
        homeBrandPartnerAdapter.setHomeBrandPartnerInfos(homeBradPartnnerInfos);
    }

    public HomeBrandPartnerView(Context context) {
        this(context, null);
    }

    public HomeBrandPartnerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeBrandPartnerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
        initListener();
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_brand_partner, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
    }

    private void initData() {
        ScrollGridLayoutManager gridLayoutManager = new ScrollGridLayoutManager(getContext(), 4);
        gridLayoutManager.setScrollEnable(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        homeBrandPartnerAdapter = new HomeBrandPartnerAdapter(getContext());
        mRecyclerView.setAdapter(homeBrandPartnerAdapter);
    }

    private void initListener() {
        morePartnerView.setOnClickListener(v -> MorePartnerActivity.startActivity(getContext()));
        homeBrandPartnerAdapter.setOnItemClickListener(new HomeBrandPartnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, HomeBrandPartnerInfo data) {
                UserManager userManager = App.getInstance().getManager(UserManager.class);
                if (position==0) {
                    if(userManager.isLogin())BrowserActivity.startActivity((Activity) getContext(), SystemConfig.carUrl, data.getName());
                    else LoginActivity.startActivity((Activity) getContext());
                } else BrowserActivity.startActivity((Activity) getContext(), data.getUrl(), data.getName());
            }
        });
    }
}
