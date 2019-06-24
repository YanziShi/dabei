package com.qckj.dabei.ui.mine.auth;

import android.Manifest;
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
import com.qckj.dabei.ui.home.BusinessCircleActivity;
import com.qckj.dabei.ui.home.SelectStreetActivity;
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
public class EnterpriseAuthActivity extends BaseActivity {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.edit_people_name)
    EditText editPeopleName;

    @FindViewById(R.id.edit_name)
    EditText editName;

    @FindViewById(R.id.edit_number)
    EditText editNumber;

    @FindViewById(R.id.text_type)
    TextView textTradeType;  //服务行业

    @FindViewById(R.id.text_city)
    TextView textCity;

    @FindViewById(R.id.text_street)
    TextView textStreet;

    @FindViewById(R.id.text_circle)
    TextView textCircle;

    @FindViewById(R.id.edit_address)
    EditText editAddress;


    @FindViewById(R.id.image_cover)
    ImageView imageCover;

    @FindViewById(R.id.image_certificate)
    ImageView imageCert;

    @Manager
    UserManager userManager;

    @Manager
    GaoDeLocationManager gaoDeLocationManager;
    UserLocationInfo mUserLocationInfo;

    private BottomDialog dialog;

    private String imageUrlCover;
    private String imageUrlCert;

    String tradeId;
    String streetId;
    String circleId = "";

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
    protected static final int REQUEST_CODE_STREET = 4;
    protected static final int REQUEST_CODE_CIRCLE = 5;
    protected ImageHelper mImageHelper;

    int imageType;
    AuthenticationInfo authInfo;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EnterpriseAuthActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, AuthenticationInfo authenticationInfo) {
        Intent intent = new Intent(context, EnterpriseAuthActivity.class);
        intent.putExtra("authInfo",authenticationInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise_auth);
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
                if (function == FUNCTION_TEXT_RIGHT && actionBar.getRightText().equals("提交")){
                    if (editPeopleName.getText().toString().equals("")) {
                        showToast("请填写法人代表名！");
                        editPeopleName.requestFocus();
                        return true;
                    }
                    if (editName.getText().toString().equals("")) {
                        showToast("请填写企业名称！");
                        editName.requestFocus();
                        return true;
                    }
                    if (editNumber.getText().toString().equals("")) {
                        showToast("请填写企业电话！");
                        editNumber.requestFocus();
                        return true;
                    }
                    if (textTradeType.getText().toString().equals("")) {
                        showToast("请选择企业行业！");
                        return true;
                    }

                    if (textCity.getText().toString().equals("")) {
                        showToast("请选择所在城市！");
                        return true;
                    }

                    if (textStreet.getText().toString().equals("")) {
                        showToast("请选择所在街道！");
                        return true;
                    }

                    if (editAddress.getText().toString().equals("")) {
                        showToast("请输入详细地址！");
                        return true;
                    }

                    if (imageUrlCover == null) {
                        showToast("请拍企业封面！");
                        return true;
                    }
                    if (imageUrlCert == null) {
                        showToast("请拍营业执照！");
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
                    editPeopleName.setEnabled(true);
                    textStreet.setEnabled(true);
                    textCircle.setEnabled(true);
                    imageCert.setEnabled(true);
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
        editPeopleName.setEnabled(false);
        textStreet.setEnabled(false);
        textCircle.setEnabled(false);
        imageCert.setEnabled(false);

        editName.setText(authInfo.getShopName());
        editNumber.setText(authInfo.getShopPhone());
        editAddress.setText(authInfo.getAddress());
        editPeopleName.setText(authInfo.getPeopleName());
        textCity.setText(authInfo.getOfficeAddr());
        textTradeType.setText(authInfo.getShopTypeName());
        //textStreet.setText(authInfo.getStreetName());
        //textCircle.setText(authInfo.getBusiDistrictName());
        Glide.with(this).load(authInfo.getShopImage()).into(imageCover);
        imageUrlCover = authInfo.getShopImage();
        Glide.with(this).load(authInfo.getCertificateImage()).into(imageCert);
        imageUrlCert = authInfo.getCertificateImage();
        tradeId = authInfo.getShopTypeId();
        streetId = authInfo.getStreetId();
        circleId = authInfo.getBusiDistrictId();
    }

    void submitInfo() {
        showLoadingProgressDialog();
        new MerchantCertificationRequester("1", tradeId, textCity.getText().toString(), editNumber.getText().toString(),
                editName.getText().toString(), imageUrlCover, userManager.getCurId(), mUserLocationInfo.getLatitude(),
                mUserLocationInfo.getLongitude(), editPeopleName.getText().toString(), editAddress.getText().toString(),
                streetId, circleId, imageUrlCert, new OnHttpResponseCodeListener<JSONObject>() {
            @Override
            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                super.onHttpResponse(isSuccess, jsonObject, message);
                dismissLoadingProgressDialog();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                if (isSuccess) {
                    userManager.getUserInfo().setEntAuthState(UserInfo.USER_AUTH_STATE_ING);
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

    @OnClick({R.id.image_cover, R.id.text_city, R.id.text_type, R.id.text_street, R.id.text_circle, R.id.image_certificate})
    private void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.image_cover:
                imageType = 1;
                setCardPhoto();
                break;
            case R.id.image_certificate:
                imageType = 2;
                setCardPhoto();
                break;
            case R.id.text_city:
                AddressPicker.showAddrPicker( EnterpriseAuthActivity.this,new AddressPicker.ClickListener() {
                    @Override
                    public void onClick(String address) {
                        textCity.setText(address);
                        //清空街道和商圈
                        streetId = null;
                        textStreet.setText("");

                        circleId = "";
                        textCircle.setText("");
                    }
                });
                break;
            case R.id.text_type:
                Intent intent1 = new Intent(this, MoreCategoryActivity.class);
                intent1.putExtra("notToNext", true);
                startActivityForResult(intent1, REQUEST_CODE_TYPE);
                break;
            case R.id.text_street:
                Intent intentStreet = new Intent(getActivity(), SelectStreetActivity.class);
                intentStreet.putExtra("notToNext", true);
                startActivityForResult(intentStreet, REQUEST_CODE_STREET);
                break;
            case R.id.text_circle:
                if(streetId==null) Toast.makeText(getActivity(),"请先选择你所在街道！",Toast.LENGTH_SHORT).show();
                else{
                    Intent intentCircle = new Intent(getActivity() , BusinessCircleActivity.class);
                    intentCircle.putExtra("streetId",streetId);
                    startActivityForResult(intentCircle,REQUEST_CODE_CIRCLE);
                }
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
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKE_PHOTO:
                    if (imageType == 1) Glide.with(this).load(mImageHelper.getCameraFilePath()).into(imageCover);
                    else Glide.with(this).load(mImageHelper.getCameraFilePath()).into(imageCert);
                    Bitmap bitmap = CommonUtils.lessenUriImage(mImageHelper.getCameraFilePath());
                    uploadFile(bitmap);
                    break;
                case REQUEST_CODE_CHOOSE_PHOTO:
                    if (imageType == 1) Glide.with(this).load(data.getData()).into(imageCover);
                    else Glide.with(this).load(data.getData()).into(imageCert);
                    Bitmap bitmap1 = CommonUtils.lessenUriImage(ImageHelper.getFilePathFromUri(data.getData()));
                    uploadFile(bitmap1);
                    break;
                case REQUEST_CODE_TYPE:
                    tradeId = data.getStringExtra("id");
                    textTradeType.setText(data.getStringExtra("name"));
                    break;
                case REQUEST_CODE_STREET:
                    streetId = data.getStringExtra("streetId");
                    textStreet.setText(data.getStringExtra("streetName"));

                    circleId = "";
                    textCircle.setText("");

                    break;
                case REQUEST_CODE_CIRCLE:
                    circleId = data.getStringExtra("F_C_ID");
                    textCircle.setText(data.getStringExtra("F_C_NAME"));
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
            if(imageType==1)imageUrlCover = jsonObject.getString("url");
            else imageUrlCert = jsonObject.getString("url");

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

