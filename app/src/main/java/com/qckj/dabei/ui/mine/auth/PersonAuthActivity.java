package com.qckj.dabei.ui.mine.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.view.dialog.BottomDialog;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.auth.MerchantCertificationRequester;
import com.qckj.dabei.model.merchant.AuthenticationInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.home.MoreCategoryActivity;
import com.qckj.dabei.view.AddressPicker;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.ImageHelper;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.qckj.dabei.view.ActionBar.FUNCTION_TEXT_RIGHT;

/**
 * Created by yangzhizhong on 2019/5/13.
 */
public class PersonAuthActivity extends BaseActivity {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.edit_name)
    EditText editName;

    @FindViewById(R.id.edit_number)
    EditText editNumber;

    @FindViewById(R.id.text_type)
    TextView textTradeType;  //服务行业

    @FindViewById(R.id.text_city)
    TextView textCity;

    @FindViewById(R.id.edit_address)
    EditText editAddress;


    @FindViewById(R.id.image_cover)
    ImageView imageCover;

    @Manager
    UserManager userManager;

    @Manager
    GaoDeLocationManager gaoDeLocationManager;
    UserLocationInfo mUserLocationInfo;

    private BottomDialog dialog;

    private String imageUrl;

    String tradeId;

    protected static final int REQUEST_CODE_PERMISSION_CHOOSE_PHOTO = 1;
    protected static final int REQUEST_CODE_PERMISSION_TAKE_PHOTO = 2;
    /**
     * 从相册选择图片
     */
    protected static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    /**
     * 调用相机拍照
     */
    protected static final int REQUEST_CODE_TAKE_PHOTO = 2;
    protected static final int REQUEST_CODE_TYPE = 3;
    protected ImageHelper mImageHelper;

    AuthenticationInfo authInfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonAuthActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,AuthenticationInfo authenticationInfo) {
        Intent intent = new Intent(context, PersonAuthActivity.class);
        intent.putExtra("authInfo",authenticationInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_auth);
        ViewInject.inject(this);
        authInfo = (AuthenticationInfo) getIntent().getSerializableExtra("authInfo");
        if(authInfo!=null) initView();

        dialog = new BottomDialog(this);
        mUserLocationInfo = gaoDeLocationManager.getUserLocationInfo();
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(getExternalCacheDir(), "TakePhoto");
        mImageHelper = new ImageHelper(takePhotoDir);

        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if (function == FUNCTION_TEXT_RIGHT&&actionBar.getRightText().equals("提交")) {
                    if (editName.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "请填写姓名！", Toast.LENGTH_SHORT).show();
                        editName.requestFocus();
                        return true;
                    }
                    if (textTradeType.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "请选择服务类型！", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (textCity.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "请选择所在城市！", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (editAddress.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "请输入详细地址！", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    if (imageUrl == null) {
                        Toast.makeText(getApplicationContext(), "请拍服务封面！", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    submitInfo();
                }else{
                    actionBar.setRightText("提交");
                    editName.setEnabled(true);
                    editNumber.setEnabled(true);
                    editAddress.setEnabled(true);
                    textCity.setEnabled(true);
                    textTradeType.setEnabled(true);
                    imageCover.setEnabled(true);
                }
                return false;
            }
        });
    }

    void initView(){
        actionBar.setRightText("重新认证");
        editName.setEnabled(false);
        editNumber.setEnabled(false);
        editAddress.setEnabled(false);
        textCity.setEnabled(false);
        textTradeType.setEnabled(false);
        imageCover.setEnabled(false);

        editName.setText(authInfo.getShopName());
        editNumber.setText(authInfo.getShopPhone());
        editAddress.setText(authInfo.getAddress());
        textCity.setText(authInfo.getOfficeAddr());
        textTradeType.setText(authInfo.getShopTypeName());
        Glide.with(this).load(authInfo.getShopImage()).into(imageCover);
        imageUrl = authInfo.getShopImage();
        tradeId = authInfo.getShopTypeId();

    }

    void submitInfo() {
        showLoadingProgressDialog();
        new MerchantCertificationRequester("2", tradeId, textCity.getText().toString(), editNumber.getText().toString(),
                editName.getText().toString(),imageUrl, userManager.getCurId(), mUserLocationInfo.getLatitude(),
                mUserLocationInfo.getLongitude(), "", editAddress.getText().toString(),
                "", "", "", new OnHttpResponseCodeListener<JSONObject>() {
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                dismissLoadingProgressDialog();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if (isSuccess) {
                    userManager.getUserInfo().setPerAuthState(UserInfo.USER_AUTH_STATE_ING);
                    EventBus.getDefault().post(CommonUtils.CHANGE_USERINFO_TAG);
                    finish();
                }
            }

            @Override
            public void onLocalErrorResponse(int code) {
                dismissLoadingProgressDialog();
                super.onLocalErrorResponse(code);
            }
        }).doPost();
    }

    private void setCardPhoto() {
        dialog.show();
        dialog.setOnTakePhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                dialog.dismiss();
            }
        });
        dialog.setOnChoosePhotoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
                dialog.dismiss();
            }
        });
    }

    @OnClick({R.id.image_cover, R.id.text_city,R.id.text_type})
    private void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.image_cover:
                setCardPhoto();
                break;
            case R.id.text_city:
                AddressPicker.showAddrPicker( PersonAuthActivity.this,new AddressPicker.ClickListener() {
                    @Override
                    public void onClick(String address) {
                        textCity.setText(address);
                    }
                });
                break;
            case R.id.text_type:
                Intent intent1 = new Intent(this, MoreCategoryActivity.class);
                intent1.putExtra("notToNext",true);
                startActivityForResult(intent1, REQUEST_CODE_TYPE);
                break;
        }
    }

    /**
     * 选择系统相册
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_CHOOSE_PHOTO)
    public void choosePhoto() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startActivityForResult(mImageHelper.getChooseSystemGalleryIntent(), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "请开起存储空间权限，以正常使用", REQUEST_CODE_PERMISSION_CHOOSE_PHOTO, perms);
        }
    }

    /**
     * 调用相机拍照
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TAKE_PHOTO)
    public void takePhoto() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            try {
                startActivityForResult(mImageHelper.getTakePhotoIntent(getApplicationContext()), REQUEST_CODE_TAKE_PHOTO);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "不支持拍照", Toast.LENGTH_SHORT).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, "请开起存储空间和相机权限，以正常使用", REQUEST_CODE_PERMISSION_TAKE_PHOTO, perms);
        }
    }

    /**
     * 拍照上传
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKE_PHOTO:
                    Glide.with(this).load(mImageHelper.getCameraFilePath()).into(imageCover);
                    Bitmap bitmap = CommonUtils.lessenUriImage(mImageHelper.getCameraFilePath());
                    uploadFile(bitmap);
                    break;
                case REQUEST_CODE_CHOOSE_PHOTO:
                    Glide.with(this).load(data.getData()).into(imageCover);
                    Bitmap bitmap1 = CommonUtils.lessenUriImage(ImageHelper.getFilePathFromUri(data.getData()));
                    uploadFile(bitmap1);
                    break;
                case REQUEST_CODE_TYPE:
                    tradeId = data.getStringExtra("id");
                    textTradeType.setText(data.getStringExtra("name"));
                    break;
                default:
                    break;
            }
        }
    }

    void uploadFile(Bitmap bitmap) {
        File saveFile = new File(getExternalCacheDir(), "realName.jpg");
        CommonUtils.compressBitmapToFile(bitmap, saveFile);
        showLoadingProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                parseJson(CommonUtils.uploadFile("/alyFile",saveFile));
            }
        }).start();
        if (!bitmap.isRecycled()) {
            bitmap.recycle();  //回收图片所占的内存
            System.gc(); //提醒系统及时回收
        }
    }

    private void parseJson(String json) {
        try {
            Looper.prepare();
            dismissLoadingProgressDialog();
            Looper.loop();
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
            imageUrl = jsonObject.getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
