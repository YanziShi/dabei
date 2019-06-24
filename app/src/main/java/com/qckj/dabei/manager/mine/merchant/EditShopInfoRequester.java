package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/16.
 */
public class EditShopInfoRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private int isGold;
    private String videoUrl;
    private String fmimg;
    private String brandUrl;
    private String url;
    private String actUrl;
    private String message;
    private String imgs;

    public EditShopInfoRequester(String userId, int isGold, String videoUrl, String fmimg, String brandUrl, String url, String actUrl,
                                 String message, String imgs, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.isGold = isGold;
        this.videoUrl = videoUrl;
        this.fmimg = fmimg;
        this.brandUrl = brandUrl;
        this.url = url;
        this.actUrl = actUrl;
        this.message = message;
        this.imgs = imgs;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/editShopInformation");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
        params.put("isGold", isGold);
        params.put("videoUrl", videoUrl);
        params.put("fmimg", fmimg);
        params.put("brandUrl", brandUrl);
        params.put("url", url);
        params.put("actUrl", actUrl);
        params.put("message", message);
        params.put("imgs", imgs);
    }
}