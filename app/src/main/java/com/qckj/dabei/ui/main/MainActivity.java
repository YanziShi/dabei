package com.qckj.dabei.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.qckj.dabei.BuildConfig;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.main.fragment.DemandLibFragment;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.ui.main.fragment.HomeFragment;
import com.qckj.dabei.ui.main.fragment.ServiceBusinessFragment;
import com.qckj.dabei.ui.main.fragment.ReleaseDemandFragment;
import com.qckj.dabei.ui.mine.auth.AuthCenterActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.TabItemView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TabItemView.OnTabItemStateWillChangeListener {

    public static final String KEY_INDEX = "KEY_INDEX";

    public static final int INDEX_NONE = -1;
    public static final int INDEX_HOME = 0;
    public static final int INDEX_DEMAND_LIB = 1;
    public static final int INDEX_RELEASE_DEMAND = 2;
    public static final int INDEX_NEARBY = 3;
    public static final int INDEX_MINE = 4;

    private static final long TIME_SPACE = 3000;

    @FindViewById(R.id.tab_home_view)
    private TabItemView mTabHome;

    @FindViewById(R.id.tab_lib_view)
    private TabItemView mTabLib;

    @FindViewById(R.id.tab_nearby_view)
    private TabItemView mTabNearby;

    @FindViewById(R.id.tab_mine_view)
    private TabItemView mTabMine;

    @FindViewById(R.id.tab_release_view)
    private TabItemView mTabRelease;

    @Manager
    UserManager userManager;

    @Manager
    GaoDeLocationManager gaoDeLocationManager;

    private List<TabItemView> tabs = new ArrayList<>();

    /**
     * 返回间隔
     */
    private long mBackPressedTime;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, @HomeTabIndexMode int index) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_INDEX, index);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInject.inject(this);
        if(TextUtils.isEmpty(gaoDeLocationManager.getUserLocationInfo().getProvince())){
            gaoDeLocationManager.getUserLocationInfo().setAdCode("520102");
            gaoDeLocationManager.getUserLocationInfo().setCity("贵阳市");
            gaoDeLocationManager.getUserLocationInfo().setDistrict("南明区");
            gaoDeLocationManager.getUserLocationInfo().setLatitude(26.562832);
            gaoDeLocationManager.getUserLocationInfo().setLongitude(106.718932);
            gaoDeLocationManager.getUserLocationInfo().setProvince("贵州省");
        }
        initTab();
        handleNewIntent(getIntent());
    }

    private void initTab() {
        mTabHome.setFragmentClass(HomeFragment.class);
        mTabLib.setFragmentClass(DemandLibFragment.class);
        mTabRelease.setFragmentClass(ReleaseDemandFragment.class);
        mTabNearby.setFragmentClass(ServiceBusinessFragment.class);
        mTabMine.setFragmentClass(MineFragment.class);

        mTabHome.setOnTabItemStateWillChangeListener(this);
        mTabLib.setOnTabItemStateWillChangeListener(this);
        mTabRelease.setOnTabItemStateWillChangeListener(this);
        mTabNearby.setOnTabItemStateWillChangeListener(this);
        mTabMine.setOnTabItemStateWillChangeListener(this);

        tabs.add(mTabHome);
        tabs.add(mTabLib);
        tabs.add(mTabRelease);
        tabs.add(mTabNearby);
        tabs.add(mTabMine);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        long ct = SystemClock.uptimeMillis();
        if (ct - mBackPressedTime >= TIME_SPACE) {
            mBackPressedTime = ct;
            showToast(R.string.exit_application);
        } else {
            finish();
        }
    }

    private void handleNewIntent(Intent intent) {
        int tabId = intent.getIntExtra(KEY_INDEX, INDEX_HOME);
        changeTabTag(tabId);
    }

    private void changeTabTag(@HomeTabIndexMode int index) {
        if (isFinishing()) {
            return;
        }
        if (index != INDEX_NONE) {
            setIndexWithoutException(index);
        }
    }

    // 选中某项标签
    private void setIndexWithoutException(@IntRange(from = 0) int index) {
        if (BuildConfig.DEBUG) {
            if (index >= tabs.size() || index < 0) {
                throw new RuntimeException("index >= tabs.size() || index < 0, current index = " + index);
            }
        }
        try {
            TabItemView tab = tabs.get(index);
            tab.setItemSelected(!tab.isItemSelected());
            setIndex(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setIndex(int index) throws Exception {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < tabs.size(); i++) {
            TabItemView tab = tabs.get(i);
            Class<? extends Fragment> fragmentClass = tab.getFragmentClass();
            String tag = fragmentClass.getName();
            Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
            if (i == index) {
                tab.setItemSelected(true);
                // 添加tab
                if (fragmentByTag == null) {
                    fragmentTransaction.add(R.id.container, fragmentClass.newInstance(), tag);
                } else {
                    if (fragmentByTag.isDetached()) {
                        fragmentTransaction.attach(fragmentByTag);
                    }
                }
            } else if (tab.isItemSelected()) {
                tab.setItemSelected(false);
                // 移除tab
                if (fragmentByTag != null) {
                    fragmentTransaction.detach(fragmentByTag);
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean shouldChangeTabItemState(TabItemView tabItemView) {
        if (!tabItemView.isItemSelected()) {
            String className = tabItemView.getFragmentClass().getSimpleName();
            if (className.equals(ReleaseDemandFragment.class.getSimpleName())) {
                if (!userManager.isLogin() ) {
                    LoginActivity.startActivity(getActivity());
                    return false;
                }
                if(userManager.getUserInfo().getAuthState()!= UserInfo.USER_AUTH_STATE){
                    AuthCenterActivity.startActivity(getActivity());
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onTabItemStatChanged(TabItemView tabItemView) {
        int index = 0;
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i) == tabItemView) {
                index = i;
                break;
            }
        }
        setIndexWithoutException(index);
    }

    @IntDef({INDEX_NONE, INDEX_HOME, INDEX_DEMAND_LIB, INDEX_RELEASE_DEMAND, INDEX_NEARBY, INDEX_MINE})
    public @interface HomeTabIndexMode {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
