package com.qckj.dabei.app.http;

import android.support.annotation.NonNull;

import com.qckj.dabei.BuildConfig;
import com.qckj.dabei.util.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 基于http的请求
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public abstract class HttpRequester {

    public static final Logger logger = new Logger("WEB");

    private StringResponseHandler responseHandler = new StringResponseHandler() {
        @Override
        public void onResult(int code, String content) {
            if (BuildConfig.DEBUG) {
                String serverContent = content == null ? "null" : content.replace("%", "%%");
                String string = String.format(Locale.getDefault(), "response, url = %s, code = %d, content = %s", getServerUrl(), code, serverContent);
                logger.i(string);
            }

            try {
                if (code == HttpURLConnection.HTTP_OK) {
                    JSONObject jsonObject = new JSONObject(content);
                    HttpRequester.this.onResult(parseCode(jsonObject), jsonObject);
                } else {
                    onError(new IOException("HTTP CODE NOT 200"));
                }
            } catch (JSONException e) {
                onError(e);
            }
        }

        @Override
        public void onError(Exception exception) {
            if (BuildConfig.DEBUG) {
                String string = String.format(Locale.getDefault(), "response, url = %s, error = %s", getServerUrl(), exception);
                logger.i(string);
            }
            HttpRequester.this.onError(exception);
        }
    };

    protected boolean parseCode(JSONObject jsonObject) {
        return jsonObject.optBoolean("success");
    }

    @NonNull
    public abstract String getServerUrl();

    /**
     * 发起POST请求
     */
    public void doPost() {
        doPost(5);
    }

    /**
     * 发起POST请求
     */
    public void doPost(int delay) {
        String url = getServerUrl();
        Map<String, String> params = changeParams(getParams());
        Map<String, String> header = params;
        if (BuildConfig.DEBUG) {
            String string = String.format(Locale.getDefault(), "request, url = %s", url + "?" + makeQueryString(params));
            logger.i(string);
        }
        AsyncHttpClient.postDelay(url, params, header, delay, responseHandler);
    }

    /**
     * 发起GET请求
     */
    public void doGet() {
        String url = getServerUrl();
        Map<String, String> params = changeParams(getParams());
        Map<String, String> header = getHeader(params);
        if (BuildConfig.DEBUG) {
            String string = String.format(Locale.getDefault(), "request, url = %s", url + "?" + makeQueryString(params));
            logger.i(string);
        }
        AsyncHttpClient.get(url, params, header, responseHandler);
    }

    protected Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        onPutParams(params);
        return params;
    }

    protected Map<String, String> getHeader(Map<String, String> params) {
        return null;
    }

    protected Map<String, String> changeParams(Map<String, Object> params) {
        Map<String, String> newParams = new HashMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            newParams.put(entry.getKey(), entry.getValue().toString());
        }
        return newParams;
    }

    public String makeQueryString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (i != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(params.get(key));
        }
        return stringBuilder.toString();
    }

    /**
     * 放入请求参数,data_list中的参数
     *
     * @param params 参数  基本参数已经放入
     */
    protected abstract void onPutParams(Map<String, Object> params);

    /**
     * 请求成功了  服务器已经返回结果
     *
     * @param isSuccess 请求成功与否，，一般code等于200表示请求成功
     * @param content   从服务器获取到的数据
     */
    protected abstract void onResult(boolean isSuccess, JSONObject content) throws JSONException;

    /**
     * 请求失败了
     *
     * @param exception 失败原因
     */
    protected abstract void onError(Exception exception);

}
