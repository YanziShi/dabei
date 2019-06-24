package com.qckj.dabei.ui.mine.complain;

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

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.OnResult;
import com.qckj.dabei.manager.mine.complain.SubmitSuggestionRequester;
import com.qckj.dabei.manager.mine.user.UserManager;
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

/**
 * 投诉反馈
 * <p>
 * Created by yangzhizhong on 2019/4/12.
 */
public class ComplainActivity extends BaseActivity {

    private static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;

    @FindViewById(R.id.action_bar)
    private ActionBar actionBar;

    @FindViewById(R.id.consumer_tv)
    private TextView consumerTv;

    @FindViewById(R.id.service_tv)
    private TextView serviceTv;

    @FindViewById(R.id.agent_tv)
    private TextView agentTv;

    @FindViewById(R.id.opinion_et)
    private EditText opinionEt;

    @FindViewById(R.id.image_grid_list)
    private RecyclerView imageGridList;

    @Manager
    private UserManager userManager;

    private int type = 0;
    private SuggestionImageAdapter suggestionImageAdapter;
    private long curTime;
    private List<SuggestionImageAdapter.ImageInfo> mImageInfos;
    private List<String> pathList = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ComplainActivity.class);
        context.startActivity(intent);
    }

    private SelectPhotoTypeDialog.OnSelectPhotoListener selectPhotoListener = new SelectPhotoTypeDialog.OnSelectPhotoListener() {
        @Override
        public void onSelectPhoto(boolean isTakePhoto) {
            if (isTakePhoto) {
                checkCameraPermission();
            } else {
                AlbumUtils.openAlbum(ComplainActivity.this, AlbumUtils.NORMAL);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        ViewInject.inject(this);
        init();
        initListener();
    }

    private void initListener() {
        suggestionImageAdapter.setCallback(new SimpleSuggestionImageCallback() {
            @Override
            public void optionClick() {
                SelectPhotoTypeDialog selectPhotoTypeDialog = new SelectPhotoTypeDialog(getActivity());
                selectPhotoTypeDialog.show();
                selectPhotoTypeDialog.setOnSelectPhotoListener(selectPhotoListener);
            }
        });

        actionBar.setOnActionBarClickListener(new ActionBar.OnActionBarClickListener() {
            @Override
            public boolean onActionBarClick(int function) {
                if (function == ActionBar.FUNCTION_TEXT_RIGHT) {
                    submitInfo();
                    return true;
                }
                return false;
            }
        });
    }

    private void submitInfo() {
        mImageInfos = suggestionImageAdapter.getData();
        String opinion = opinionEt.getText().toString().trim();
        if (type == 0) {
            showToast("请选择身份");
        } else if (TextUtils.isEmpty(opinion)) {
            showToast("请输入意见");
        } else if (mImageInfos.size() == 0) {
            showToast("请上传图片");
        } else {
            showLoadingProgressDialog();
            uploadImageFile(new OnResult<List<String>>() {
                @Override
                public void onResult(boolean isSuccess, List<String> pathList) {
                    if (isSuccess) {
                        new SubmitSuggestionRequester(type + "", opinion, CommonUtils.buildPath(pathList), userManager.getCurId(), new OnHttpResponseCodeListener<Void>() {
                            @Override
                            public void onHttpResponse(boolean isSuccess, Void aVoid, String message) {
                                super.onHttpResponse(isSuccess, aVoid, message);
                                dismissLoadingProgressDialog();
                                showToast(message);
                                if (isSuccess) finish();
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

    private void init() {
        imageGridList.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        suggestionImageAdapter = new SuggestionImageAdapter(getActivity(), 4);
        imageGridList.setAdapter(suggestionImageAdapter);
    }


    @OnClick({R.id.consumer_tv, R.id.service_tv, R.id.agent_tv})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.consumer_tv:
                setSelect(0);
                type = 1;
                break;
            case R.id.service_tv:
                setSelect(1);
                type = 2;
                break;
            case R.id.agent_tv:
                setSelect(2);
                type = 3;
                break;
        }
    }

    private void setSelect(int type) {
        consumerTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_unappraise_circlel));
        consumerTv.setTextColor(getResources().getColor(R.color.black));
        serviceTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_unappraise_circlel));
        serviceTv.setTextColor(getResources().getColor(R.color.black));
        agentTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_unappraise_circlel));
        agentTv.setTextColor(getResources().getColor(R.color.black));
        if (type == 0) {
            consumerTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_appraise_circlel));
            consumerTv.setTextColor(getResources().getColor(R.color.bg_yellow_end));
        } else if (type == 1) {
            serviceTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_appraise_circlel));
            serviceTv.setTextColor(getResources().getColor(R.color.bg_yellow_end));
        } else {
            agentTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_appraise_circlel));
            agentTv.setTextColor(getResources().getColor(R.color.bg_yellow_end));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AlbumUtils.OPEN_ALBUM:
                if (resultCode == RESULT_OK && data != null) {
                    String picturePath = AlbumUtils.getDirFromAlbumUri(data.getData());
                    SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                    imageInfo.imagePath = picturePath;
                    imageInfo.isAddPictureItem = false;
                    suggestionImageAdapter.addItem(imageInfo);
                }
                break;
            case AlbumUtils.OPEN_CAMERA:
                if (resultCode == RESULT_OK) {
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    File file = new File(externalStorageDirectory, curTime + ".png");
                    SuggestionImageAdapter.ImageInfo imageInfo = new SuggestionImageAdapter.ImageInfo();
                    imageInfo.imagePath = file.getPath();
                    imageInfo.isAddPictureItem = false;
                    suggestionImageAdapter.addItem(imageInfo);
                }
                break;
        }

    }

    /**
     * 检查相机权限
     */
    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            curTime = System.currentTimeMillis();
            AlbumUtils.openCamera(ComplainActivity.this, AlbumUtils.NORMAL, curTime + "");
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
                AlbumUtils.openCamera(ComplainActivity.this, AlbumUtils.NORMAL, curTime + "");
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
