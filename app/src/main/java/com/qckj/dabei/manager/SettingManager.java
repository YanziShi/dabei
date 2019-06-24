package com.qckj.dabei.manager;

import android.content.SharedPreferences;
import android.support.v4.util.LruCache;

import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;

/**
 * 设置保存  用于保存用户设置
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class SettingManager extends BaseManager {

    // 用户信息
    public static final String KEY_USER_INFO = "key_user_info";

    // 缓存用户的定位信息
    public static final String KEY_USER_LOCATION_INFO = "key_user_location_info";

    // 整个应用程序配置
    private SharedPreferences applicationPreferences;

    @Override
    public void onManagerCreate(App app) {
        applicationPreferences = getApplication().getSharedPreferences("dabei_application_settings", 0);
    }

    // 保存用户配置
    public void putSetting(String key, String value) {
        applicationPreferences.edit().putString(key, value).apply();
    }

    public String getSetting(String key, String defValue) {
        return applicationPreferences.getString(key, defValue);
    }

    // 保存用户配置
    public void putSetting(String key, boolean value) {
        applicationPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getSetting(String key, boolean defValue) {
        return applicationPreferences.getBoolean(key, defValue);
    }
}
