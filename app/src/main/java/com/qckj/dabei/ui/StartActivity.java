package com.qckj.dabei.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.manager.StartAdvertiseRequester;
import com.qckj.dabei.manager.location.GaoDeLocationManager;
import com.qckj.dabei.manager.location.UserLocationInfo;
import com.qckj.dabei.model.StartAdvertiseInfo;
import com.qckj.dabei.ui.main.MainActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.webview.BrowserActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * 闪屏页面
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class StartActivity extends BaseActivity {
    @FindViewById(R.id.image_advertise)
    ImageView imageAdvertise;

    @FindViewById(R.id.text_time)
    TextView textTime;

    String url;

    @Manager
    private GaoDeLocationManager gaoDeLocationManager;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ViewInject.inject(this);
        checkLocationPermission();
        gaoDeLocationManager.requireLocation(userLocationInfo -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            },1000);
        });
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String value = String.valueOf((int) (millisUntilFinished / 1000));
                textTime.setText(value+" 跳过");
            }

            @Override
            public void onFinish() {
                MainActivity.startActivity(StartActivity.this);
                finish();
            }
        };
    }


    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ArrayList<String> newPermissionList = new ArrayList<>();
            for(String mPermission:mPermissionList){
                if(ActivityCompat.checkSelfPermission(this,mPermission)!=PackageManager.PERMISSION_GRANTED){
                    newPermissionList.add(mPermission);
                }
            }
            ActivityCompat.requestPermissions(this, newPermissionList.toArray(new String[newPermissionList.size()]), 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void loadData(){
        String city = gaoDeLocationManager.getUserLocationInfo().getCity();
        String district = gaoDeLocationManager.getUserLocationInfo().getDistrict();
        new StartAdvertiseRequester(city, district, new OnHttpResponseCodeListener<StartAdvertiseInfo>() {
            @Override
            public void onHttpResponse(boolean isSuccess, StartAdvertiseInfo startAdvertiseInfo, String message) {
                super.onHttpResponse(isSuccess, startAdvertiseInfo, message);
                if (isSuccess) {
                    Glide.with(getActivity()).load(startAdvertiseInfo.getImg()).into(imageAdvertise);
                    imageAdvertise.setClickable(true);
                    textTime.setVisibility(View.VISIBLE);
                    url = startAdvertiseInfo.getUrl();
                    countDownTimer.start();
                }
            }
        }).doPost();
    }

    @OnClick({R.id.image_advertise,R.id.text_time})
    void onViewClick(View view) {
        switch (view.getId()){
            case R.id.image_advertise:
                MainActivity.startActivity(StartActivity.this);
                BrowserActivity.startActivity(StartActivity.this, url, "广告");
                finish();
                break;
            case R.id.text_time:
                MainActivity.startActivity(StartActivity.this);
                finish();
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
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
