package com.qckj.dabei.ui.mine.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.mine.user.GetUserInfoByIdRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.AuthenticationRequester;
import com.qckj.dabei.manager.mine.merchant.MerchantInformationRequester;
import com.qckj.dabei.manager.mine.merchant.UpdateBusinessStateRequester;
import com.qckj.dabei.model.merchant.AuthenticationInfo;
import com.qckj.dabei.model.merchant.MerchantInfo;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.ui.mine.auth.EnterpriseAuthActivity;
import com.qckj.dabei.ui.mine.auth.PersonAuthActivity;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.image.CircleImageView;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

import static com.umeng.socialize.utils.ContextUtil.getContext;

/**
 * 商家中心
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class MerchantCenterActivity extends BaseActivity {

    @FindViewById(R.id.switch_state)
    Switch swichState;

    @FindViewById(R.id.user_head_portrait)
    private CircleImageView mUserHeadPortrait;

    @FindViewById(R.id.user_name)
    private TextView mUserName;

    @FindViewById(R.id.is_auth_view)
    private ImageView isAuthView;

    @FindViewById(R.id.member_grade_view)
    private ImageView memberGradeView;

    @FindViewById(R.id.is_gold_business)
    private ImageView isGold;

    @FindViewById(R.id.shop_stick_view)
    private CommonItemView itemTop;

    @FindViewById(R.id.opening_gold_merchants_view)
    private CommonItemView itemGold;

    @Manager
    private UserManager userManager;
    MerchantInfo merchantInfo;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MerchantCenterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_center);
        ViewInject.inject(this);
        EventBus.getDefault().register(this);
        initUserInfo(MineFragment.userInfo);
        loadData();
    }

    void loadData(){
        new MerchantInformationRequester(userManager.getCurId(), new OnHttpResponseCodeListener<List<MerchantInfo>>() {
            @Override
            public void onHttpResponse(boolean isSuccess, List<MerchantInfo> merchantInfos, String message) {
                super.onHttpResponse(isSuccess, merchantInfos, message);
                if (isSuccess) {
                    merchantInfo = merchantInfos.get(0);
                    initMerchantView();
                }
            }
        }).doPost();
    }

    void initMerchantView(){
        if(merchantInfo.getBusiness_state()==1) swichState.setChecked(true);
        else swichState.setChecked(false);
        swichState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) changeBusinessState(1);
                else changeBusinessState(0);
            }
        });

        if(merchantInfo.getIsdpzd()==1) itemTop.setContent("已置顶");
        else itemTop.setContent("未置顶");

        if(merchantInfo.getIsjpsj()==1) itemGold.setContent("已开通");
        else itemGold.setContent("未开通");

    }

    void changeBusinessState(int state){
        new UpdateBusinessStateRequester(userManager.getCurId(),state,new OnHttpResponseCodeListener<JSONObject>(){
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        }).doPost();
    }

    private void initUserInfo(UserByIdInfo userInfo) {
        if (!TextUtils.isEmpty(userInfo.getF_C_ID())) {
            if (TextUtils.isEmpty(userInfo.getF_C_NICHENG())) {
                mUserName.setText(R.string.mine_app_user_name);
            } else {
                mUserName.setText(userInfo.getF_C_NICHENG());
            }
            GlideUtil.displayImage(getContext(), userInfo.getF_C_TXIMG(), mUserHeadPortrait, R.mipmap.ic_mine_default_user_photo, R.mipmap.ic_mine_default_user_photo);

            isAuthView.setVisibility(View.VISIBLE);
            memberGradeView.setVisibility(View.VISIBLE);
            if(userInfo.getF_I_RZSJ_QY()==UserInfo.USER_AUTH_STATE) isAuthView.setImageResource(R.mipmap.ic_me_business);
            else if(userInfo.getF_I_RZSJ_GR()==UserInfo.USER_AUTH_STATE) isAuthView.setImageResource(R.mipmap.ic_me_personal);
            else if(userInfo.getF_I_RZSJ_QY()==UserInfo.USER_AUTH_STATE) isAuthView.setImageResource(R.mipmap.ic_me_realname);
            else isAuthView.setVisibility(View.GONE);

            if(userInfo.getMember_grade() == UserInfo.MEMBER_GRADE_DIAMOND) memberGradeView.setImageResource(R.mipmap.ic_me_masonry);
            else if(userInfo.getMember_grade() == UserInfo.MEMBER_GRADE_GOLD) memberGradeView.setImageResource(R.mipmap.ic_me_gold);
            else if(userInfo.getMember_grade() == UserInfo.MEMBER_GRADE_SILVER) memberGradeView.setImageResource(R.mipmap.ic_me_silver);
            else memberGradeView.setVisibility(View.GONE);

            if(userInfo.getIsjpsj()==1) {
                isGold.setVisibility(View.VISIBLE);
                isGold.setImageResource(R.mipmap.ic_me_gold_medal);
            }else isGold.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.shop_edit_view,R.id.shop_pre_view,R.id.mine_service_view,R.id.auth_info_view,R.id.shop_stick_view,
            R.id.opening_gold_merchants_view})
    private void onViewClick(View v){
        switch(v.getId()){
            case R.id.shop_edit_view:
                EditShopActivity.startActivity(MerchantCenterActivity.this);
                break;
            case R.id.shop_pre_view:
                String isJpsj = "";
                if(merchantInfo.getIsjpsj()==1) isJpsj = "gold";
                else isJpsj = "general";
                String url = SystemConfig.webUrl+"/#/merchant?shopId="+ merchantInfo.getF_C_ID()
                        +"&type="+ isJpsj
                        + "&userId="+ userManager.getCurId()
                        +"&poi="+ gaoDeLocationManager.getUserLocationInfo().getLongitude()+","+
                        gaoDeLocationManager.getUserLocationInfo().getLatitude();
                BrowserActivity.startActivity(MerchantCenterActivity.this,url,false);
                break;
            case R.id.mine_service_view:
                MyServiceActivity.startActivity(MerchantCenterActivity.this);
                break;
            case R.id.auth_info_view:
                showLoadingProgressDialog();
                new AuthenticationRequester(userManager.getCurId(),new OnHttpResponseCodeListener<AuthenticationInfo>(){
                    @Override
                    public void onHttpResponse(boolean isSuccess, AuthenticationInfo authenticationInfo, String message) {
                        super.onHttpResponse(isSuccess, authenticationInfo, message);
                        dismissLoadingProgressDialog();
                        if(isSuccess){
                            if(authenticationInfo.getAuthType().equals("1"))EnterpriseAuthActivity.startActivity(MerchantCenterActivity.this,authenticationInfo);
                            else PersonAuthActivity.startActivity(MerchantCenterActivity.this,authenticationInfo);
                        }else showToast(message);
                    }

                    @Override
                    public void onLocalErrorResponse(int code) {
                        super.onLocalErrorResponse(code);
                    }
                }).doPost();
                break;
            case R.id.shop_stick_view:
                ShopTopActivity.startActivity(MerchantCenterActivity.this,MineFragment.userInfo.getF_C_BALANCE());
                break;
            case R.id.opening_gold_merchants_view:
                GoldMerchantActivity.startActivity(MerchantCenterActivity.this,merchantInfo.getIsjpsj(),MineFragment.userInfo.getF_C_BALANCE());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String bundle) {
        if(bundle.equals(CommonUtils.CHANGE_USERINFO_TAG)){
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
