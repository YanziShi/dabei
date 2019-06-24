package com.qckj.dabei.manager.cache;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.App;
import com.qckj.dabei.app.BaseManager;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存管理
 * <p>
 * Created by yangzhizhong on 2019/3/26.
 */
public class CacheManager extends BaseManager {

    /** 首页功能点信息 */
    public static final String KEY_HOME_FUNCTION_INFO = "key_home_function_info";

    @Override
    public void onManagerCreate(App app) {

    }

    private ACache getCache() {
        return ACache.get(App.getInstance(), "cache_");
    }

    /**
     * 把数据放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, String value) {
        getCache().put(key, value);
    }

    /**
     * 把数据放入缓存
     *
     * @param key      键 {@link CacheManager}中定义
     * @param value    值
     * @param saveTime 数据保存时间， 单位为秒
     */
    public void put(String key, String value, int saveTime) {
        getCache().put(key, value, saveTime);
    }

    /**
     * 将lis的数据放入缓存中
     *
     * @param key  缓存的KEY
     * @param list 需要缓存的数据
     */
    public <V> void putList(String key, List<V> list) {
        if (list == null) {
            return;
        }
        JSONArray jsonArray = new JSONArray();
        for (Object object : list) {
            JSONObject jsonObject = JsonHelper.toJSONObject(object);
            if (jsonObject != null) {
                jsonArray.put(jsonObject);
            }
        }
        put(key, jsonArray.toString());
    }

    /**
     * 获取缓存的数据
     *
     * @param key 缓存的key
     * @param cls 缓存的数据类型
     * @param <V> 数据类型  建议全部使用｛@link JsonField｝注解
     * @return 返回JSON数据
     */
    @NonNull
    public <V> List<V> getList(String key, Class<V> cls) {
        List<V> list = new ArrayList<>();
        String jsonStr = get(key, "");
        if ("".equals(jsonStr)) {
            return list;
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            list = JsonHelper.toList(jsonArray, cls);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * 从缓存中获取数据
     *
     * @param key          键
     * @param defaultValue 如果缓存中没有数据，则返回该值
     */
    public String get(String key, String defaultValue) {
        String result = getCache().getAsString(key);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 清除缓存
     */
    public void clear() {
        getCache().clear();
    }

}
