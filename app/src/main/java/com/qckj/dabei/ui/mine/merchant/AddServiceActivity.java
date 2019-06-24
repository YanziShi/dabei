package com.qckj.dabei.ui.mine.merchant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.InsertGoodsRequester;
import com.qckj.dabei.model.merchant.MyServiceInfo;
import com.qckj.dabei.ui.mine.complain.SimpleSuggestionImageCallback;
import com.qckj.dabei.ui.mine.complain.adapter.SuggestionImageAdapter;
import com.qckj.dabei.util.AlbumUtils;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.ActionBar;
import com.qckj.dabei.view.dialog.SelectPhotoTypeDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.qckj.dabei.view.ActionBar.FUNCTION_TEXT_RIGHT;

/**
 * Created by yangzhizhong on 2019/5/17.
 */
public class AddServiceActivity extends BaseActivity {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.eidt_name)
    EditText editName;
    @FindViewById(R.id.eidt_price)
    EditText editPrice;
    @FindViewById(R.id.eidt_unit)
    EditText editUnit;
    @FindViewById(R.id.image_service)
    ImageView imageService;

    @FindViewById(R.id.eidt_brief)
    EditText editBrief;
    @FindViewById(R.id.image_grid)
    RecyclerView imageGrid;
    private long curTime;

    String imageUrl = "";

    private SuggestionImageAdapter suggestionImageAdapter;
    private List<SuggestionImageAdapter.ImageInfo> mImageInfos;
    private List<String> pathList = new ArrayList<>();
    private String imgs = "";
    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;
    @Manager
    UserManager userManager;
    MyServiceInfo myServiceInfo;
    String goodsid = "";
    int type;    //用来区分是哪一个视图调用相机功能

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddServiceActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, MyServiceInfo myServiceInfo) {
        Intent intent = new Intent(context, AddServiceActivity.class);
        intent.putExtra("data", myServiceInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        ViewInject.inject(this);
        imageGrid.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        suggestionImageAdapter = new SuggestionImageAdapter(getActivity(), 4);
        imageGrid.setAdapter(suggestionImageAdapter);

        myServiceInfo = (MyServiceInfo) getIntent().getSerializableExtra("data");
        if (myServiceInfo != null) initView();
        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if (function == FUNCTION_TEXT_RIGHT) {
                    submitInfo();
                }
                return false;
            }
        });
        suggestionImageAdapter.setCallback(new SimpleSuggestionImageCallback() {
            @Override
            public void optionClick() {
                type = 1;
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
            }
        });

    }

    void initView() {
        actionBar.setTitleText("修改服务");
        goodsid = myServiceInfo.getGoodsid();
        editName.setText(myServiceInfo.getServiceName());
        editPrice.setText(String.valueOf(myServiceInfo.getMoney()));
        editUnit.setText(myServiceInfo.getServiceUnit());
        editBrief.setText(myServiceInfo.getMessage());
        Glide.with(this).load(myServiceInfo.getServiceCover()).into(imageService);
        if(myServiceInfo.getImgs()!=null){
            String[] urls = myServiceInfo.getImgs().split(",");
            for (String url : urls) {
                SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                imageInfo.imagePath = url;
                imageInfo.isAddPictureItem = false;
                suggestionImageAdapter.addItem(imageInfo);
            }
        }
        imageUrl = myServiceInfo.getServiceCover();
    }

    private SelectPhotoTypeDialog.OnSelectPhotoListener selectPhotoListener = new SelectPhotoTypeDialog.OnSelectPhotoListener() {
        @Override
        public void onSelectPhoto(boolean isTakePhoto) {
            if (isTakePhoto) {
                checkCameraPermission();
            } else {
                AlbumUtils.openAlbum(AddServiceActivity.this, AlbumUtils.NORMAL);
            }
        }
    };

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            curTime = System.currentTimeMillis();
            AlbumUtils.openCamera(AddServiceActivity.this, AlbumUtils.NORMAL, curTime + "");
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

    private void submitInfo() {
        if(TextUtils.isEmpty(editName.getText())){
            showToast("请填写姓名");
            return;
        }
        if(TextUtils.isEmpty(editPrice.getText())){
            showToast("请填写价格");
            return;
        }
        if(TextUtils.isEmpty(editUnit.getText())){
            showToast("请填写单位");
            return;
        }
        if(TextUtils.isEmpty(imageUrl)){
            showToast("请拍服务封面");
            return;
        }
        mImageInfos = suggestionImageAdapter.getData();
        showLoadingProgressDialog();
        uploadImageFile(new OnResult<List<String>>() {
            @Override
            public void onResult(boolean isSuccess, List<String> pathList) {
                if (isSuccess) {
                    imgs = CommonUtils.buildPath(pathList);
                    new InsertGoodsRequester(userManager.getCurId(),
                            editName.getText().toString(),
                            Double.valueOf(editPrice.getText().toString()),
                            editUnit.getText().toString(), imageUrl, imgs, editBrief.getText().toString(), goodsid, new OnHttpResponseCodeListener<JSONObject>() {
                        @Override
                        public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                            super.onHttpResponse(isSuccess, jsonObject, message);
                            dismissLoadingProgressDialog();
                            showToast(message);
                            if(isSuccess) finish();
                        }

                        @Override
                        public void onLocalErrorResponse(int code) {
                            super.onLocalErrorResponse(code);
                            dismissLoadingProgressDialog();
                        }
                    }).doPost();
                } else {
                    dismissLoadingProgressDialog();
                }
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AlbumUtils.OPEN_ALBUM:
                if (resultCode == RESULT_OK && data != null) {
                    String picturePath = AlbumUtils.getDirFromAlbumUri(data.getData());
                    if (type == 0) {
                        Glide.with(AddServiceActivity.this).load(picturePath).into(imageService);
                        uploadImage(new File(picturePath));
                    } else {
                        SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                        imageInfo.imagePath = picturePath;
                        imageInfo.isAddPictureItem = false;
                        suggestionImageAdapter.addItem(imageInfo);
                    }
                }
                break;
            case AlbumUtils.OPEN_CAMERA:
                if (resultCode == RESULT_OK) {
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    File file = new File(externalStorageDirectory, curTime + ".png");
                    if (type == 0) {
                        Glide.with(AddServiceActivity.this).load(file.getPath()).into(imageService);
                        uploadImage(file);
                    } else {
                        SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                        imageInfo.imagePath = file.getPath();
                        imageInfo.isAddPictureItem = false;
                        suggestionImageAdapter.addItem(imageInfo);
                    }
                }
                break;
        }
    }

    void uploadImage(File file) {
        showLoadingProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = CommonUtils.uploadFile("/alyFile",file);
                try {
                    dismissLoadingProgressDialog();
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
                        imageUrl = jsonObject.getString("url");
                    }else{ showToast(jsonObject.getString("mes"));}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnClick({R.id.image_service})
    private void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.image_service:
                type = 0;
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
                break;
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
