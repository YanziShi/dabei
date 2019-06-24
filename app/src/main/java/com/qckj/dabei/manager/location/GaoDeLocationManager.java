package com.qckj.dabei.manager.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qckj.dabei.R;
import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;
import com.qckj.dabei.manager.SettingManager;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 位置管理器
 * 调用该类应该在主线程中调用
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class GaoDeLocationManager extends BaseManager {

    private UserLocationInfo userLocationInfo;
    private SettingManager settingManager;

    private List<OnLocationListener> onLocationCompleteListeners = new ArrayList<>();

    /**
     * 是否正在定位
     */
    private boolean isLocation = false;

    private boolean isHaveShow = false;

    /**
     * 获取用户的定位信息
     *
     * @return 用当前的位置信息
     * 注意：该方法可能返回null,定位失败或者是未开始定位
     */
    @Nullable
    public UserLocationInfo getUserLocationInfo() {
        return userLocationInfo;
    }

    @Override
    public void onManagerCreate(App app) {
        settingManager = getManager(SettingManager.class);
        // 复用之前的定位信息
        String locationInfo = settingManager.getSetting(SettingManager.KEY_USER_LOCATION_INFO, "");
        if (!TextUtils.isEmpty(locationInfo)) {
            try {
                userLocationInfo = JsonHelper.toObject(new JSONObject(locationInfo), UserLocationInfo.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取位置，并确保位置信息获取成功
     *
     * @param onLocationCompleteListener 定位完成的回调
     */
    public void requireLocation(@Nullable OnLocationListener onLocationCompleteListener) {
        if (onLocationCompleteListener != null) {
            onLocationCompleteListeners.add(onLocationCompleteListener);
        }

        if (isLocation) {
            return;
        }

        // 超时
        AMapLocationClient mLocationClient = new AMapLocationClient(App.getInstance());

        AMapLocationClientOption mLocationClientOption = new AMapLocationClientOption();
        //mLocationClientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClientOption.setGpsFirst(true);//GPS优先
        mLocationClientOption.setNeedAddress(true); // //设置是否返回地址信息（默认返回地址信息）
        // mLocationClientOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationClientOption);

        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation != null && !TextUtils.isEmpty(aMapLocation.getProvince())) {
                    // 初始化UserLocationInfo
                    userLocationInfo = new UserLocationInfo();
                    userLocationInfo.setProvince(aMapLocation.getProvince());
                    userLocationInfo.setCity(aMapLocation.getCity());
                    userLocationInfo.setDistrict(aMapLocation.getDistrict());
                    userLocationInfo.setLatitude(aMapLocation.getLatitude());
                    userLocationInfo.setLongitude(aMapLocation.getLongitude());
                    userLocationInfo.setAdCode(aMapLocation.getAdCode());
                    // 持久化定位信息，以便下次使用
                    settingManager.putSetting(SettingManager.KEY_USER_LOCATION_INFO, JsonHelper.toJSONObject(userLocationInfo).toString());
                }else{
                    userLocationInfo = new UserLocationInfo();
                    userLocationInfo.setAdCode("520102");
                    userLocationInfo.setCity("贵阳市");
                    userLocationInfo.setDistrict("南明区");
                    userLocationInfo.setLatitude(26.562832);
                    userLocationInfo.setLongitude(106.718932);
                    userLocationInfo.setProvince("贵州省");
                }

                mLocationClient.onDestroy();
                isLocation = false;
                for (OnLocationListener locationCompleteListener : onLocationCompleteListeners) {
                    locationCompleteListener.onLocationComplete(userLocationInfo);
                }
                onLocationCompleteListeners.clear();
            }
        });
        mLocationClient.startLocation();
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    public boolean isLocationOpen() {
        LocationManager locationManager
                = (LocationManager) App.getInstance().getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 显示定位服务对话框
     *
     * @param context
     */
    private void showLocationDialog(Context context) {
        if (isHaveShow) {
            return;
        }
        isHaveShow = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.location_dialog_title));
        builder.setMessage(context.getResources().getString(R.string.location_dialog_message));
        builder.setNegativeButton(context.getResources().getString(R.string.location_dialog_i_know), null);
        builder.setPositiveButton(context.getResources().getString(R.string.location_dialog_to_set), (dialogInterface, i) -> startSetting(context, false));
        builder.show();
    }

    /**
     * 检查定位服务是否开启 没有开启则弹出对话框
     *
     * @return
     */
    public boolean checkLocationOpen(Context context) {
        if (isLocationOpen()) {
            return checkLocationPermission(context);
        } else {
            showLocationDialog(context);
            return false;
        }

    }

    private void startSetting(Context context, boolean isSetting) {
        Intent intent = new Intent();
        if (isSetting) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getApplication().getPackageName(), null));
        } else {
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            //如果页面无法打开，进入设置页面
            if (!isSetting) {
                startSetting(context, true);
            }
        }
    }

    private boolean checkLocationPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getResources().getString(R.string.location_dialog_auth_title));
            builder.setMessage(context.getResources().getString(R.string.location_dialog_auth_message));
            builder.setNegativeButton(context.getResources().getString(R.string.location_dialog_i_know), null);
            builder.setPositiveButton(context.getResources().getString(R.string.location_dialog_to_set), (dialogInterface, i) -> startSetting(context, true));
            builder.show();
            return false;
        }

    }

}
