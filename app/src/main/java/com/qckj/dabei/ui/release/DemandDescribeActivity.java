package com.qckj.dabei.ui.release;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.release.SubmitDemandPost;
import com.qckj.dabei.ui.mine.complain.SimpleSuggestionImageCallback;
import com.qckj.dabei.ui.mine.complain.adapter.SuggestionImageAdapter;
import com.qckj.dabei.ui.mine.order.MineOrderActivity;
import com.qckj.dabei.util.AlbumUtils;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.dialog.SelectPhotoTypeDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 需求描述
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class DemandDescribeActivity extends BaseActivity {

    public static final String KEY_DEMAND_TYPE = "demand_type";
    public static final String KEY_CLASS_ID = "key_class_id";

    @FindViewById(R.id.input_describe)
    private EditText inputDescribe;

    @FindViewById(R.id.image_grid_list)
    private RecyclerView imageGridList;

    @FindViewById(R.id.text_address)
    private TextView textAddress;

    @FindViewById(R.id.edit_price)
    private EditText editPrice;

    @FindViewById(R.id.text_type)
    private TextView textType;

    @Manager
    private UserManager userManager;
    @Manager
    GaoDeLocationManager gaoDeLocationManager;
    private String classId;

    private SuggestionImageAdapter suggestionImageAdapter;
    private long curTime;
    private List<SuggestionImageAdapter.ImageInfo> mImageInfos;
    private List<String> pathList = new ArrayList<>();
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;
    private static final int SEARCHREQUESTCODE = 1001;

    String address;
    String addressname;
    double longitude;
    double latitude;

    public static void startActivity(Context context, String demandType, String classId) {
        Intent intent = new Intent(context, DemandDescribeActivity.class);
        intent.putExtra(KEY_DEMAND_TYPE, demandType);
        intent.putExtra(KEY_CLASS_ID, classId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_describe);
        ViewInject.inject(this);
        String demandType = getIntent().getStringExtra(KEY_DEMAND_TYPE);
        classId = getIntent().getStringExtra(KEY_CLASS_ID);
        textType.setText(demandType);
        address = gaoDeLocationManager.getUserLocationInfo().getProvince() + gaoDeLocationManager.getUserLocationInfo().getCity()
                + gaoDeLocationManager.getUserLocationInfo().getDistrict();
        latitude = gaoDeLocationManager.getUserLocationInfo().getLatitude();
        longitude = gaoDeLocationManager.getUserLocationInfo().getLongitude();
        addressname = gaoDeLocationManager.getUserLocationInfo().getCity()+"-"+gaoDeLocationManager.getUserLocationInfo().getDistrict();

        textAddress.setText(address);

        imageGridList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        suggestionImageAdapter = new SuggestionImageAdapter(getActivity(), 4);
        imageGridList.setAdapter(suggestionImageAdapter);
        suggestionImageAdapter.setCallback(new SimpleSuggestionImageCallback() {
            @Override
            public void optionClick() {
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
            }
        });
    }

    private void commitDemandInfo() {
        mImageInfos = suggestionImageAdapter.getData();
        String des = inputDescribe.getText().toString().trim();
        String money = editPrice.getText().toString().trim();
        if(TextUtils.isEmpty(des)||TextUtils.isEmpty(money)){
            showToast("请填写完整信息！");
            return;
        }
        showLoadingProgressDialog();
        uploadImageFile(new OnResult<List<String>>() {
            @Override
            public void onResult(boolean isSuccess, List<String> pathList) {
                if (isSuccess) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                            JSONObject params = new JSONObject();
                            params.put("usid", userManager.getCurId());
                            params.put("classifytwoId", classId);
                            params.put("money", money);
                            params.put("address", address);
                            params.put("mes",des );
                            params.put("imgs", CommonUtils.buildPath(pathList));
                            params.put("usphone", userManager.getUserInfo().getAccount());
                            params.put("longitude", longitude);
                            params.put("latitude", latitude);
                            params.put("addressname", addressname);
                            parseJson(SubmitDemandPost.post(params));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    dismissLoadingProgressDialog();
                }
            }
        });
    }

    private void parseJson(String json) {
        try {
            dismissLoadingProgressDialog();
            JSONObject jsonObject = new JSONObject(json);
            boolean isSuccess = jsonObject.getBoolean("success");
            if (isSuccess) {
                MineOrderActivity.startActivity(DemandDescribeActivity.this, 0);
                finish();
            }else {
                Looper.prepare();
                showToast(jsonObject.getString("mes"));
                Looper.loop();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.text_address, R.id.commit_demand})
    void onClickView(View view) {
        switch (view.getId()) {
            case R.id.text_address:
                startActivityForResult(new Intent(DemandDescribeActivity.this, SelectAddressActivity.class), SEARCHREQUESTCODE);
                break;
            case R.id.commit_demand:
                commitDemandInfo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AlbumUtils.OPEN_ALBUM: {
                        String picturePath = AlbumUtils.getDirFromAlbumUri(data.getData());
                        SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                        imageInfo.imagePath = picturePath;
                        imageInfo.isAddPictureItem = false;
                        suggestionImageAdapter.addItem(imageInfo);
                    }
                    break;
                case AlbumUtils.OPEN_CAMERA: {
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    File file = new File(externalStorageDirectory, curTime + ".png");
                    SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                    imageInfo.imagePath = file.getPath();
                    imageInfo.isAddPictureItem = false;
                    suggestionImageAdapter.addItem(imageInfo);
                    }
                break;
                case SEARCHREQUESTCODE:
                    PoiItem poiItem = (PoiItem) data.getParcelableExtra("poiItem");
                    address = poiItem.getSnippet();
                    longitude = poiItem.getLatLonPoint().getLongitude();
                    latitude  = poiItem.getLatLonPoint().getLatitude();
                    addressname = poiItem.getCityName()+"-"+poiItem.getAdName();
                    textAddress.setText(address);
                    break;
            }
        }

    }

    /**
     * 上传图片
     */
    SuggestionImageAdapter.ImageInfo imageInfo;
    int position;
    private void uploadImageFile(final OnResult<List<String>> listener) {
        imageInfo = null;
        if (pathList.size() == mImageInfos.size()) {
            listener.onResult(true, pathList);
            return;
        }
        pathList.clear();
        for (int i=0 ; i<mImageInfos.size();i++) {
            SuggestionImageAdapter.ImageInfo mImageInfo = mImageInfos.get(i);
            position = i;
            if(mImageInfo.imagePath.startsWith("http")){
                pathList.add(mImageInfo.imagePath);
            } else if (TextUtils.isEmpty(mImageInfo.imageId)) {
                imageInfo = mImageInfo;
                break;
            }
        }
        if (imageInfo == null) {
            listener.onResult(true, pathList);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String json = CommonUtils.uploadFile("/alyFile", new File(imageInfo.imagePath));
                    try {
                        if(json==null) {
                            Looper.prepare();
                            showToast("您上传的图片有误，请重新拍照、相册选择！");
                            dismissLoadingProgressDialog();
                            Looper.loop();
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(json);
                        boolean isSuccess = jsonObject.getBoolean("success");
                        if (isSuccess) {
                            mImageInfos.get(position).imagePath=jsonObject.getString("url");
                            uploadImageFile(listener);
                        } else {
                            listener.onResult(false, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private SelectPhotoTypeDialog.OnSelectPhotoListener selectPhotoListener = new SelectPhotoTypeDialog.OnSelectPhotoListener() {
        @Override
        public void onSelectPhoto(boolean isTakePhoto) {
            if (isTakePhoto) {
                checkCameraPermission();
            } else {
                AlbumUtils.openAlbum(DemandDescribeActivity.this, AlbumUtils.NORMAL);
            }
        }
    };

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            curTime = System.currentTimeMillis();
            AlbumUtils.openCamera(DemandDescribeActivity.this, AlbumUtils.NORMAL, curTime + "");
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
                curTime = System.currentTimeMillis();
                AlbumUtils.openCamera(DemandDescribeActivity.this, AlbumUtils.NORMAL, curTime + "");
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
