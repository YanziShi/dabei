package com.qckj.dabei.ui.mine.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResultMessageListener;
import com.qckj.dabei.manager.mine.merchant.MerchantInformationRequester;
import com.qckj.dabei.manager.mine.user.GetUserInfoByIdRequester;
import com.qckj.dabei.manager.mine.user.UploadHeadPortraitRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.merchant.MerchantInfo;
import com.qckj.dabei.model.mine.UserByIdInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.main.MainActivity;
import com.qckj.dabei.ui.main.fragment.MineFragment;
import com.qckj.dabei.util.AlbumUtils;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.CommonItemView;
import com.qckj.dabei.view.dialog.SelectPhotoTypeDialog;
import com.qckj.dabei.view.dialog.SelectSexDialog;
import com.qckj.dabei.view.image.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 用户信息界面
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class UserInfoActivity extends BaseActivity {

    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;
    private static final int REQUEST_CODE_CHANGE_NAME = 10;
    private static final int REQUEST_CODE_CHANGE_PHONE = 11;
    private static final int REQUEST_CODE_CHANGE_PASSWORD = 12;

    @FindViewById(R.id.user_head_portrait)
    private CircleImageView mUserHeadPortrait;

    @FindViewById(R.id.user_name)
    private CommonItemView mUserName;

    @FindViewById(R.id.user_sex)
    private CommonItemView mUserSex;

    @FindViewById(R.id.user_phone)
    private CommonItemView mUserPhone;

    @FindViewById(R.id.modify_password)
    private CommonItemView mModifyPassword;

    @Manager
    private UserManager userManager;
    UserByIdInfo userInfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    private SelectPhotoTypeDialog.OnSelectPhotoListener selectPhotoListener = new SelectPhotoTypeDialog.OnSelectPhotoListener() {
        @Override
        public void onSelectPhoto(boolean isTakePhoto) {
            if (isTakePhoto) {
                checkCameraPermission();
            } else {
                AlbumUtils.openAlbum(UserInfoActivity.this, AlbumUtils.NORMAL);
            }
        }
    };

    private SelectSexDialog.OnSelectSexListener selectSexListener = new SelectSexDialog.OnSelectSexListener() {
        @Override
        public void onSelectSex(boolean isMale) {
            showLoadingProgressDialog();
            userManager.modifyUserSex(isMale, new OnResultMessageListener() {
                @Override
                public void onResult(boolean isSuccess, String message) {
                    dismissLoadingProgressDialog();
                    if (isSuccess) {
                        EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                        if (isMale) {
                            mUserSex.setContent(getString(R.string.mine_sex_male));
                        } else {
                            mUserSex.setContent(getString(R.string.mine_sex_female));
                        }
                    } else {
                        showToast(message);
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ViewInject.inject(this);
        userInfo = MineFragment.userInfo;
        initUserInfo();
    }

    private void initUserInfo() {
        GlideUtil.displayImage(getActivity(), userInfo.getF_C_TXIMG(), mUserHeadPortrait, R.mipmap.ic_mine_default_user_photo);
        if (TextUtils.isEmpty(userInfo.getF_C_NICHENG())) {
            mUserName.setContent(getString(R.string.mine_app_user_name));
        } else {
            mUserName.setContent(userInfo.getF_C_NICHENG());
        }
        int sex = userInfo.getF_I_SEX();
        switch (sex) {
            case UserInfo.SEX_FEMAIL:
                mUserSex.setContent(getString(R.string.mine_sex_female));
                break;
            case UserInfo.SEX_MAIL:
                mUserSex.setContent(getString(R.string.mine_sex_male));
                break;
            case UserInfo.SEX_NO:
                mUserSex.setContent(getString(R.string.mine_sex_no));
                break;
        }
        String account = userInfo.getF_C_PHONE();
        if (TextUtils.isEmpty(account)) {
            mUserPhone.setContent(getString(R.string.mine_bind_phone_no));
        } else {
            mUserPhone.setContent(account);
        }
        int pwdState = userInfo.getPasswordState();
        switch (pwdState) {
            case UserInfo.USER_PWD_STATE_NO:
                mModifyPassword.setTitle("设置登录密码");
                mModifyPassword.setContent(getString(R.string.setting_no));
                break;
            case UserInfo.USER_PWD_STATE_YES:
                mModifyPassword.setTitle("修改登录密码");
                mModifyPassword.setContent(getString(R.string.setting_yes));
                break;
        }
    }


    @OnClick({R.id.user_head_portrait_ll, R.id.user_name, R.id.user_sex, R.id.user_phone, R.id.modify_password, R.id.exit_login})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.user_head_portrait_ll:
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
                break;
            case R.id.user_name:
                ModifyUserNameActivity.startActivity(getActivity(),REQUEST_CODE_CHANGE_NAME);
                break;
            case R.id.user_sex:
                SelectSexDialog selectSexDialog = new SelectSexDialog(getActivity());
                selectSexDialog.show();
                selectSexDialog.setOnSelectSexListener(selectSexListener);
                break;
            case R.id.user_phone:
                AgainBindPhoneActivity.startActivity(getActivity(),REQUEST_CODE_CHANGE_PHONE);
                break;
            case R.id.modify_password:
                ModifyLoginPwdActivity.startActivity(getActivity(),userInfo.getPasswordState(),REQUEST_CODE_CHANGE_PASSWORD);
                break;
            case R.id.exit_login:
                userManager.logout();
                MainActivity.startActivity(getActivity());
                EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode) {
                case AlbumUtils.OPEN_ALBUM:
                    if (data != null && resultCode == RESULT_OK) {
                        Glide.with(getActivity()).load(data.getData()).into(mUserHeadPortrait);
                        uploadPhoto(AlbumUtils.getDirFromAlbumUri(data.getData()));
                        break;
                    }
                case AlbumUtils.OPEN_CAMERA:
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    File file = new File(externalStorageDirectory, "head.png");
                    Glide.with(getActivity()).load(file.getPath()).into(mUserHeadPortrait);
                    uploadPhoto(file.getPath());
                    break;
                case REQUEST_CODE_CHANGE_NAME:
                    mUserName.setContent(data.getStringExtra("userName"));
                    EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                    break;
                case REQUEST_CODE_CHANGE_PHONE:
                    mUserPhone.setContent(data.getStringExtra("phoneNumber"));
                    EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                    break;
                case REQUEST_CODE_CHANGE_PASSWORD:
                    mModifyPassword.setTitle("修改登录密码");
                    mModifyPassword.setContent(getString(R.string.setting_yes));
                    EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                    break;
            }
        }
    }


    private void uploadPhoto(String path) {
        showLoadingProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseJsonImage(CommonUtils.uploadFile("/alyFile", new File(path)));
            }
        }).start();
    }

    private void parseJsonImage(String json) {
        dismissLoadingProgressDialog();
        try {
            if(json==null) {
                Looper.prepare();
                showToast("您上传的图片有误，请重新拍照、相册选择！");
                Looper.loop();
                return;
            }
            JSONObject jsonObject = new JSONObject(json);
            boolean isSuccess = jsonObject.getBoolean("success");
            if (!isSuccess) {
                Looper.prepare();
                showToast(jsonObject.getString("mes"));
                Looper.loop();
                return;
            }
            new UploadHeadPortraitRequester(userManager.getCurId(),jsonObject.getString("url"),new OnHttpResponseCodeListener<List<UserByIdInfo>>(){
                @Override
                public void onHttpResponse(boolean isSuccess, List<UserByIdInfo> userByIdInfos, String message) {
                    super.onHttpResponse(isSuccess, userByIdInfos, message);
                    showToast(message);
                    if(isSuccess) EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                }
            }).doPost();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            AlbumUtils.openCamera(UserInfoActivity.this, AlbumUtils.NORMAL,"head");
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.tip_title);
                builder.setMessage(R.string.permission_tip_camera);
                builder.setPositiveButton(R.string.tip_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AlbumUtils.openCamera(UserInfoActivity.this, AlbumUtils.NORMAL,"head");
            } else {
                showToast(R.string.permission_msg_camera_failed);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
