package com.qckj.dabei.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseFragment;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.mine.user.GetUserInfoByIdRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.PersonalInfoRequester;
import com.qckj.dabei.model.merchant.PersonalInfo;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.mine.about.AboutUsActivity;
import com.qckj.dabei.ui.mine.agent.ApplyAgentActivity;
import com.qckj.dabei.ui.mine.auth.AuthCenterActivity;
import com.qckj.dabei.ui.mine.comment.MineCommentActivity;
import com.qckj.dabei.ui.mine.complain.ComplainActivity;
import com.qckj.dabei.ui.mine.contact.EmergencyContactActivity;
import com.qckj.dabei.ui.mine.friend.InviteFriendActivity;
import com.qckj.dabei.ui.mine.merchant.MerchantCenterActivity;
import com.qckj.dabei.ui.mine.msg.MineMessageActivity;
import com.qckj.dabei.ui.mine.order.MineOrderActivity;
import com.qckj.dabei.ui.mine.partner.JoinPartnerActivity;
import com.qckj.dabei.ui.mine.resource.MineResourceActivity;
import com.qckj.dabei.ui.mine.user.LoginActivity;
import com.qckj.dabei.ui.mine.user.UserInfoActivity;
import com.qckj.dabei.ui.mine.wallet.MineWalletActivity;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.dialog.LoadingProgressDialog;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.view.image.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 我的
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class MineFragment extends BaseFragment {

    @FindViewById(R.id.mine_resource_view)
    private CommonItemView mineResourceView;

    @FindViewById(R.id.mine_message_view)
    private CommonItemView mineMessageView;

    @FindViewById(R.id.mine_join_partner_view)
    private CommonItemView mineJoinPartnerView;

    @FindViewById(R.id.mine_emergency_contact_view)
    private CommonItemView mineContactView;

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

    @FindViewById(R.id.merchant_center_btn)
    private TextView mMerchantCenterBtn;

    @Manager
    private UserManager userManager;

    @Manager
    GaoDeLocationManager gaoDeLocationManager;

    PersonalInfo personalInfo;
    static public UserByIdInfo userInfo;
    View rootView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ViewInject.inject(this, rootView);
        loadData();
        return rootView;
    }


    void loadData(){
        if(TextUtils.isEmpty(userManager.getCurId())) return;
        LoadingProgressDialog dialog = new LoadingProgressDialog(getContext());
        dialog.show();
        new GetUserInfoByIdRequester(userManager.getCurId(),new OnHttpResponseCodeListener<List<UserByIdInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserByIdInfo> userByIdInfos, String message) {
                super.onHttpResponse(isSuccess, userByIdInfos, message);
                dialog.dismiss();
                if(isSuccess){
                    userInfo = userByIdInfos.get(0);
                    initUserInfo();
                    loadPersonalInfo();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
                dialog.dismiss();
            }
        }).doPost();
    }

    void refreshData(){
        if(TextUtils.isEmpty(userManager.getCurId())) return;
        new GetUserInfoByIdRequester(userManager.getCurId(),new OnHttpResponseCodeListener<List<UserByIdInfo>>(){
            @Override
            public void onHttpResponse(boolean isSuccess, List<UserByIdInfo> userByIdInfos, String message) {
                super.onHttpResponse(isSuccess, userByIdInfos, message);
                if(isSuccess){
                    userInfo = userByIdInfos.get(0);
                    initUserInfo();
                    loadPersonalInfo();
                }
            }
            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        }).doPost();
    }

    void loadPersonalInfo(){
        new PersonalInfoRequester(userManager.getCurId(),gaoDeLocationManager.getUserLocationInfo().getCity(),
                gaoDeLocationManager.getUserLocationInfo().getDistrict(),new OnHttpResponseCodeListener<PersonalInfo>(){
            @Override
            public void onHttpResponse(boolean isSuccess, PersonalInfo data, String message) {
                super.onHttpResponse(isSuccess, data, message);
                if(isSuccess){
                    personalInfo = data;
                    initPersonalInfo(personalInfo);
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                super.onLocalErrorResponse(code);
            }
        }).doPost();
    }

    void initPersonalInfo(PersonalInfo personalInfo){
        mineResourceView.setContent(personalInfo.getZycount()+"条");
        mineMessageView.setContent(personalInfo.getMessagecount()+"条");

        if(personalInfo.getIshhr()==1)mineJoinPartnerView.setContent("已加入");
        else mineJoinPartnerView.setContent("未加入");

        if(personalInfo.getIsAddContact()==1)mineContactView.setContent("已添加");
        else mineContactView.setContent("未添加");
    }

    private void initUserInfo() {
        if (!TextUtils.isEmpty(userInfo.getF_C_ID())) {
            if (TextUtils.isEmpty(userInfo.getF_C_NICHENG())) {
                mUserName.setText(R.string.mine_app_user_name);
            } else {
                mUserName.setText(userInfo.getF_C_NICHENG());
            }
            GlideUtil.displayImage(getContext(), userInfo.getF_C_TXIMG(), mUserHeadPortrait, R.mipmap.ic_mine_default_user_photo);

            isAuthView.setVisibility(View.VISIBLE);
            memberGradeView.setVisibility(View.VISIBLE);
            if(userInfo.getF_I_RZSJ_QY()==UserInfo.USER_AUTH_STATE) isAuthView.setImageResource(R.mipmap.ic_me_business);
            else if(userInfo.getF_I_RZSJ_GR()==UserInfo.USER_AUTH_STATE) isAuthView.setImageResource(R.mipmap.ic_me_personal);
            else if(userInfo.getF_I_RZSM()==UserInfo.USER_AUTH_STATE) isAuthView.setImageResource(R.mipmap.ic_me_realname);
            else isAuthView.setVisibility(View.GONE);

            if(userInfo.getMember_grade() == UserInfo.MEMBER_GRADE_DIAMOND) memberGradeView.setImageResource(R.mipmap.ic_me_masonry);
            else if(userInfo.getMember_grade() == UserInfo.MEMBER_GRADE_GOLD) memberGradeView.setImageResource(R.mipmap.ic_me_gold);
            else if(userInfo.getMember_grade() == UserInfo.MEMBER_GRADE_SILVER) memberGradeView.setImageResource(R.mipmap.ic_me_silver);
            else memberGradeView.setVisibility(View.GONE);

            if(userInfo.getIsjpsj()==1) {
                isGold.setVisibility(View.VISIBLE);
                isGold.setImageResource(R.mipmap.ic_me_gold_medal);
            }else isGold.setVisibility(View.GONE);

            if(userInfo.getF_I_ROLE() == 2){
                mMerchantCenterBtn.setVisibility(View.VISIBLE);
            }else mMerchantCenterBtn.setVisibility(View.GONE);

        }
    }

    @OnClick({R.id.user_info_rl, R.id.merchant_center_btn, R.id.mine_resource_view,R.id.mine_order_view, R.id.mine_message_view,
            R.id.mine_earning_view, R.id.mine_join_partner_view,R.id.mine_auth_center_view,
            R.id.mine_evaluate_view, R.id.mine_emergency_contact_view, R.id.mine_complaint_feedback_view, R.id.mine_about_us_view,
            R.id.mine_apply_agent_view,R.id.mine_invite_friend_view})
    private void onViewClick(View view) {
        if (getActivity() == null) return;
        switch (view.getId()) {
            case R.id.user_info_rl:
                if (userManager.isLogin() && userInfo != null) {
                    UserInfoActivity.startActivity(getActivity());
                }else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.merchant_center_btn:
                // 商家中心
                if (userManager.isLogin() && userInfo!=null) {
                    MerchantCenterActivity.startActivity(getActivity());
                }else {
                    LoginActivity.startActivity(getActivity());
                }

                break;
            case R.id.mine_resource_view:
                if (userManager.isLogin()) {
                    if(userManager.getUserInfo().getRole() != UserInfo.ROLE_MERCHANTS){
                        MsgDialog msgDialog = new MsgDialog(getContext());
                        msgDialog.show("成为商家才能看哦！","","成为商家",false);
                        msgDialog.setOnPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                msgDialog.dismiss();
                                AuthCenterActivity.startActivity(getActivity());
                            }
                        });
                    }
                    else MineResourceActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_order_view:
                if (userManager.isLogin()) {
                    MineOrderActivity.startActivity(getActivity(),0);
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_message_view:
                if (userManager.isLogin()) {
                    MineMessageActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_earning_view:
                if (userManager.isLogin()) {
                    MineWalletActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_evaluate_view:
                if (userManager.isLogin()) {
                    MineCommentActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_join_partner_view:
                if (userManager.isLogin()) {
                    JoinPartnerActivity.startActivity(getActivity(),personalInfo.getIshhr(),userInfo.getF_C_BALANCE());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_auth_center_view:
                if (userManager.isLogin()) {
                    AuthCenterActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_emergency_contact_view:
                if (userManager.isLogin()) {
                    EmergencyContactActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_complaint_feedback_view:
                if (userManager.isLogin()) {
                    ComplainActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_about_us_view:
                AboutUsActivity.startActivity(getContext());
                break;
            case R.id.mine_apply_agent_view:
                if (userManager.isLogin()) {
                    ApplyAgentActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.mine_invite_friend_view:
                if (userManager.isLogin()) {
                    InviteFriendActivity.startActivity(getActivity());
                } else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String bundle) {
        if(bundle.equals(CommonUtils.CHANGE_USERINFO_TAG)){
            if(TextUtils.isEmpty(userManager.getCurId())) rootView =null;
            else refreshData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
