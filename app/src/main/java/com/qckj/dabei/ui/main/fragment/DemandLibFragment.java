package com.qckj.dabei.ui.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.SimpleOnPageChangeListener;
import com.qckj.dabei.manager.cache.CacheManager;
import com.qckj.dabei.manager.home.HomeDataManager;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.ui.lib.DemandLibTableFragment;
import com.qckj.dabei.ui.lib.TabEntity;
import com.qckj.dabei.ui.lib.TypeFragment;
import com.qckj.dabei.ui.lib.adapter.TableViewPagerAdapter;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 需求库
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class DemandLibFragment extends BaseFragment {

    @FindViewById(R.id.synthesis_item)
    private TextView synthesisItem;

    @FindViewById(R.id.newest_item)
    private TextView newestItem;

    @FindViewById(R.id.nearby_item)
    private TextView nearbyItem;

    @FindViewById(R.id.filtrate_item)
    private TextView filtrateItem;

    @FindViewById(R.id.demand_lib_view_pager)
    private ViewPager demandLibViewPager;

    @FindViewById(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @FindViewById(R.id.common_tab_layout)
    private CommonTabLayout commonTabLayout;

    @Manager
    private HomeDataManager homeDataManager;

    @Manager
    private CacheManager cacheManager;


    @Manager
    private GaoDeLocationManager gaoDeLocationManager;

    private List<BaseFragment> tabList = new ArrayList<>();
    private List<HomeFunctionInfo> mHomeFunctionInfos;
    private View rootView;
    private TypeFragment typeFragment;
    private String classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeFunctionInfos = cacheManager.getList(CacheManager.KEY_HOME_FUNCTION_INFO, HomeFunctionInfo.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_demand_lib, container, false);
        ViewInject.inject(this, rootView);
        initView();
        initFragment();
        setTabLayout();
        initListener();

        return rootView;
    }

    private void setTabLayout() {
        ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
        TabEntity type = new TabEntity(1000, "行业");
        customTabEntities.add(type);
        commonTabLayout.setTabData(customTabEntities);
        typeFragment = new TypeFragment();
        typeFragment.setHomeFunctionInfos(mHomeFunctionInfos);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, typeFragment);
        fragmentTransaction.commit();
    }

    private void initListener() {
        synthesisItem.setOnClickListener(v -> demandLibViewPager.setCurrentItem(0));
        newestItem.setOnClickListener(v -> demandLibViewPager.setCurrentItem(1));
        nearbyItem.setOnClickListener(v -> demandLibViewPager.setCurrentItem(2));
        filtrateItem.setOnClickListener(v -> {
            demandLibViewPager.setCurrentItem(3);
            drawerLayout.openDrawer(Gravity.END);
        });
        demandLibViewPager.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                switch (i) {
                    case 0:
                        synthesisItem.setSelected(true);
                        newestItem.setSelected(false);
                        nearbyItem.setSelected(false);
                        filtrateItem.setSelected(false);
                        break;
                    case 1:
                        synthesisItem.setSelected(false);
                        newestItem.setSelected(true);
                        nearbyItem.setSelected(false);
                        filtrateItem.setSelected(false);
                        break;
                    case 2:
                        synthesisItem.setSelected(false);
                        newestItem.setSelected(false);
                        nearbyItem.setSelected(true);
                        filtrateItem.setSelected(false);
                        break;
                    case 3:
                        synthesisItem.setSelected(false);
                        newestItem.setSelected(false);
                        nearbyItem.setSelected(false);
                        filtrateItem.setSelected(true);
                        drawerLayout.openDrawer(Gravity.END);
                        break;
                }
            }
        });
        typeFragment.setOnFiltrateItemTypeSelectListener(new TypeFragment.OnFiltrateItemTypeSelectListener() {
            @Override
            public void OnFiltrateItemTypeSelect(int position, HomeFunctionInfo.Category category) {
                classId = category.getClassId();
                DemandLibTableFragment demandLibTableFragment = (DemandLibTableFragment) tabList.get(3);
                demandLibTableFragment.setId(category.getClassId());
                drawerLayout.closeDrawers();
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (TextUtils.isEmpty(classId)) {
                    Toast.makeText(getContext(), "请选择筛选条件", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {
        synthesisItem.setSelected(true);
        newestItem.setSelected(false);
        nearbyItem.setSelected(false);
        filtrateItem.setSelected(false);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    private void initFragment() {
        for (int i = 0; i < 4; i++) {
            DemandLibTableFragment demandLibFragment = new DemandLibTableFragment();
            demandLibFragment.setUserLocationInfo(gaoDeLocationManager.getUserLocationInfo());
            demandLibFragment.setType(i + "");
            if (i == 3) {
                classId = mHomeFunctionInfos.get(0).getCategoryList().get(0).getClassId();
                demandLibFragment.setId(classId);
            }
            tabList.add(demandLibFragment);
        }
        TableViewPagerAdapter tableViewPagerAdapter = new TableViewPagerAdapter(getFragmentManager(), tabList);
        demandLibViewPager.setAdapter(tableViewPagerAdapter);
        demandLibViewPager.setCurrentItem(0);
        demandLibViewPager.setOffscreenPageLimit(2);
    }
}
