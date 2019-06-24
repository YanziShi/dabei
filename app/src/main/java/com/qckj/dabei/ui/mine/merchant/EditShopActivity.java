package com.qckj.dabei.ui.mine.merchant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.merchant.BusinessShopRequester;
import com.qckj.dabei.manager.mine.merchant.EditShopInfoRequester;
import com.qckj.dabei.model.merchant.BusinessShopInfo;
import com.qckj.dabei.ui.mine.complain.SimpleSuggestionImageCallback;
import com.qckj.dabei.ui.mine.complain.adapter.SuggestionImageAdapter;
import com.qckj.dabei.util.AlbumUtils;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
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
 * 编辑店铺
 * <p>
 * Created by yangzhizhong on 2019/4/12.
 */
public class EditShopActivity extends BaseActivity {
    @FindViewById(R.id.action_bar)
    ActionBar actionBar;

    @FindViewById(R.id.group_gold_content)
    LinearLayout groupGoldContent;
    @FindViewById(R.id.shop_video_view)
    VideoView shopVideoView;
    @FindViewById(R.id.image_grid_list)
    RecyclerView imageGridList;
    @FindViewById(R.id.eidt_brand_url)
    EditText editBrandUrl;
    @FindViewById(R.id.eidt_website_url)
    EditText editWebsiteUrl;
    @FindViewById(R.id.eidt_activity_url)
    EditText editActivityUrl;

    @FindViewById(R.id.eidt_shop_brief)
    EditText editShopBrief;
    @FindViewById(R.id.image_grid_shop)
    RecyclerView imageGridShop;

    @FindViewById(R.id.text_name)
    TextView textName;
    @FindViewById(R.id.text_phone)
    TextView textPhone;
    @FindViewById(R.id.text_type)
    TextView textType;
    @FindViewById(R.id.text_city)
    TextView textCity;
    @FindViewById(R.id.text_address)
    TextView textAddress;
    @FindViewById(R.id.image_shop)
    ImageView imageShop;

    @Manager
    UserManager userManager;

    private static final int REQUEST_CODE_VIDEO = 66;

    private SuggestionImageAdapter suggestionImageAdapter;
    private List<SuggestionImageAdapter.ImageInfo> mImageInfos;
    private List<String> pathList = new ArrayList<>();

    private SuggestionImageAdapter suggestionImageAdapterGold;
    private List<SuggestionImageAdapter.ImageInfo> mImageInfosGold;
    private List<String> pathListGold = new ArrayList<>();

    private int type = 0;   //1是金牌商家在上传照片,2是上传视频

    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;

    private long curTime;
    private String videoUrl = "";
    private String fmimg = "";
    private String imgs = "";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditShopActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop);
        ViewInject.inject(this);
        imageGridList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        suggestionImageAdapterGold = new SuggestionImageAdapter(getActivity(), 4);
        imageGridList.setAdapter(suggestionImageAdapterGold);

        imageGridShop.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        suggestionImageAdapter = new SuggestionImageAdapter(getActivity(), 4);
        imageGridShop.setAdapter(suggestionImageAdapter);

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
                type = 0;
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
            }
        });

        suggestionImageAdapterGold.setCallback(new SimpleSuggestionImageCallback() {
            @Override
            public void optionClick() {
                type = 1;
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
            }
        });

        shopVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                shopVideoView.start();
                return false;
            }
        });

        findViewById(R.id.btn_upload_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
            }
        });

        loadData();
    }

    void loadData() {
        showLoadingProgressDialog();
        new BusinessShopRequester(userManager.getCurId(), new OnHttpResponseCodeListener<BusinessShopInfo>() {
            @Override
            public void onHttpResponse(boolean isSuccess, BusinessShopInfo businessShopInfo, String message) {
                super.onHttpResponse(isSuccess, businessShopInfo, message);
                dismissLoadingProgressDialog();
                if (isSuccess) {
                    initView(businessShopInfo);
                } else showToast(message);
            }
        }).doPost();
    }

    void initView(BusinessShopInfo businessShopInfo) {
        if (businessShopInfo.getIsGold().equals("1")) {
            groupGoldContent.setVisibility(View.VISIBLE);
            if(businessShopInfo.getVideo_url()!=null) videoUrl = businessShopInfo.getVideo_url();
            if(!TextUtils.isEmpty(videoUrl)){
                shopVideoView.setVisibility(View.VISIBLE);
                shopVideoView.setVideoURI(Uri.parse(videoUrl));
                shopVideoView.start();
            }else shopVideoView.setVisibility(View.GONE);

            editBrandUrl.setText(businessShopInfo.getBrand_url());
            editWebsiteUrl.setText(businessShopInfo.getURL());
            editActivityUrl.setText(businessShopInfo.getAct_url());
            if(!TextUtils.isEmpty(businessShopInfo.getF_C_IMGS())){
                String[] urls = businessShopInfo.getF_C_IMGS().split(",");
                for (String url : urls) {
                    SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                    imageInfo.imagePath = url;
                    imageInfo.isAddPictureItem = false;
                    suggestionImageAdapterGold.addItem(imageInfo);
                }
            }

        } else {
            groupGoldContent.setVisibility(View.GONE);
        }

        editShopBrief.setText(businessShopInfo.getF_C_MESSAGE());
        if(!TextUtils.isEmpty(businessShopInfo.getFmimg())){
            String[] urls = businessShopInfo.getFmimg().split(",");
            for (String url : urls) {
                SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                imageInfo.imagePath = url;
                imageInfo.isAddPictureItem = false;
                suggestionImageAdapter.addItem(imageInfo);
            }
        }
        textName.setText(businessShopInfo.getShopName());
        textPhone.setText(businessShopInfo.getShopPhone());
        textType.setText(businessShopInfo.getCategoryName());
        textCity.setText(businessShopInfo.getCitys());
        textAddress.setText(businessShopInfo.getAddr());
        Glide.with(this).load(businessShopInfo.getShop()).into(imageShop);

    }

    private SelectPhotoTypeDialog.OnSelectPhotoListener selectPhotoListener = new SelectPhotoTypeDialog.OnSelectPhotoListener() {
        @Override
        public void onSelectPhoto(boolean isTakePhoto) {
            if (isTakePhoto) {
               checkCameraPermission();
            } else {
                 if (type == 2) {
                     Intent intent = new Intent();
                     intent.setAction(Intent.ACTION_PICK);
                     intent.setType("video/*");
                    startActivityForResult(intent, REQUEST_CODE_VIDEO);
                } else AlbumUtils.openAlbum(EditShopActivity.this, AlbumUtils.NORMAL);
            }
        }
    };

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
                    if(json==null) {
                        Looper.prepare();
                        showToast("您上传的图片有误，请重新拍照、相册选择！");
                        dismissLoadingProgressDialog();
                        Looper.loop();
                        return;
                    }
                    try {
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

    /**
     * 上传图片
     */
    SuggestionImageAdapter.ImageInfo imageInfoGold ;
    int positionGold;
    private void uploadImageFileGold(final OnResult<List<String>> listener) {
        imageInfoGold = null;
        if (pathListGold.size() == mImageInfosGold.size()) {
            listener.onResult(true, pathListGold);
            return;
        }
        pathListGold.clear();
        for ( int i=0;i<mImageInfosGold.size();i++) {
            SuggestionImageAdapter.ImageInfo mImageInfo = mImageInfosGold.get(i);
            positionGold = i;
            if(mImageInfo.imagePath.startsWith("http")) pathListGold.add(mImageInfo.imagePath);
            else if (TextUtils.isEmpty(mImageInfo.imageId)) {
                imageInfoGold = mImageInfo;
                break;
            }
        }
        if (imageInfoGold == null) {
            listener.onResult(true, pathListGold);
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String json = CommonUtils.uploadFile("/alyFile", new File(imageInfoGold.imagePath));
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
                            mImageInfosGold.get(positionGold).imagePath = jsonObject.getString("url");
                            uploadImageFileGold(listener);
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

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
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

    void openCamera(){
        if(type==2){
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            // 录制视频最大时长15s
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
            startActivityForResult(intent, REQUEST_CODE_VIDEO);
        }else{
            curTime = System.currentTimeMillis();
            AlbumUtils.openCamera(EditShopActivity.this, AlbumUtils.NORMAL, curTime + "");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                showToast(R.string.permission_msg_camera_failed);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void submitInfo() {
        showLoadingProgressDialog();
        mImageInfos = suggestionImageAdapter.getData();
        mImageInfosGold = suggestionImageAdapterGold.getData();
        uploadImageFile(new OnResult<List<String>>() {
            @Override
            public void onResult(boolean isSuccess, List<String> pathList) {
                if (isSuccess) {
                    fmimg = CommonUtils.buildPath(pathList);
                    uploadImageFileGold(new OnResult<List<String>>() {
                        @Override
                        public void onResult(boolean isSuccess, List<String> pathList) {
                            if (isSuccess) {
                                imgs = CommonUtils.buildPath(pathList);
                                new EditShopInfoRequester(userManager.getCurId(), userManager.getUserInfo().getIsGoldBusiness(),
                                        videoUrl, fmimg, editBrandUrl.getText().toString(), editWebsiteUrl.getText().toString(), editActivityUrl.getText().toString(),
                                        editShopBrief.getText().toString(), imgs, new OnHttpResponseCodeListener<JSONObject>(){
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
                                    }
                                }).doPost();

                            } else {
                                dismissLoadingProgressDialog();
                            }
                        }
                    });
                } else {
                    dismissLoadingProgressDialog();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && data == null) return;
        switch (requestCode) {
            case AlbumUtils.OPEN_ALBUM:
                String picturePath = AlbumUtils.getDirFromAlbumUri(data.getData());
                SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                imageInfo.imagePath = picturePath;
                imageInfo.isAddPictureItem = false;
                if (type == 0) {
                    suggestionImageAdapter.addItem(imageInfo);
                } else {
                    suggestionImageAdapterGold.addItem(imageInfo);
                }
                break;
            case AlbumUtils.OPEN_CAMERA:
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                File file = new File(externalStorageDirectory, curTime + ".png");
                SuggestionImageAdapter.ImageInfo imageInfo1 = new SuggestionImageAdapter.ImageInfo();
                imageInfo1.imagePath = file.getPath();
                imageInfo1.isAddPictureItem = false;
                if (type == 0) suggestionImageAdapter.addItem(imageInfo1);
                else suggestionImageAdapterGold.addItem(imageInfo1);
                break;
            case REQUEST_CODE_VIDEO:
                Uri uri = data.getData();
                Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    // 视频路径
                    String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    shopVideoView.setVisibility(View.VISIBLE);
                    shopVideoView.setVideoPath(filePath);
                    shopVideoView.start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            parseJsonVideo(CommonUtils.uploadFile("/addVideo", new File(filePath)));
                        }
                    }).start();

                    cursor.close();
                }
                break;
        }
    }

    private void parseJsonVideo(String json) {
        try {
            if(json==null) {
                Looper.prepare();
                showToast("您上传的视频有误，请重新上传！");
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
            videoUrl = jsonObject.getJSONObject("data").getString("videoUrl");

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
