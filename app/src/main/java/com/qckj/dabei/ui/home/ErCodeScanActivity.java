package com.qckj.dabei.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.camera.CameraManager;
import com.qckj.dabei.view.camera.ErCodeActivityHandler;
import com.qckj.dabei.view.camera.InactivityTimer;
import com.qckj.dabei.view.camera.ViewfinderView;
import com.umeng.analytics.MobclickAgent;

import java.util.Vector;

/**
 * 二维码扫描界面
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class ErCodeScanActivity extends BaseActivity implements SurfaceHolder.Callback {

    public static final int REQUEST_CODE_PERMISSIONS_CAMERA = 183;

    @FindViewById(R.id.er_code_scan_pre)
    private SurfaceView mSurfaceView;

    @FindViewById(R.id.viewfinder_view)
    private ViewfinderView viewfinderView;

    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;

    private ErCodeActivityHandler erCodeActivityHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_code_scan);
        ViewInject.inject(this);
        checkCameraPermission();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        CameraManager.init(getActivity());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartScan();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (erCodeActivityHandler != null) {
            erCodeActivityHandler.quitSynchronously();
            erCodeActivityHandler = null;
        }
        CameraManager.get().closeDriver();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (inactivityTimer != null) {
            inactivityTimer.shutdown();
        }
        super.onDestroy();
    }

    private void restartScan() {
        SurfaceHolder holder = mSurfaceView.getHolder();
        if (hasSurface) {
            initCamera(holder);
        } else {
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

    }

    private void initCamera(SurfaceHolder holder) {
        try {
            CameraManager.get().openDriver(holder);
        } catch (Throwable ioe) {
            return;
        }
        if (erCodeActivityHandler == null) {
            erCodeActivityHandler = new ErCodeActivityHandler(this, decodeFormats, characterSet);
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getMyHandler() {
        return erCodeActivityHandler;
    }

    public void drawViewfinder() {
        if (viewfinderView == null) {
            return;
        }
        viewfinderView.drawViewfinder();

    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String code = result.getText().toString();
        setResult(RESULT_OK,new Intent().putExtra("code",code));
        finish();
    }

    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.tip_title);
                builder.setMessage(R.string.permission_tip_camera);
                builder.setPositiveButton(R.string.tip_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showToast(R.string.permission_msg_camera_failed);
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

}
