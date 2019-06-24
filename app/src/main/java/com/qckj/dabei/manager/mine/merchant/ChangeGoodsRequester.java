package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/17.
 */
public class ChangeGoodsRequester extends SimpleBaseRequester<JSONObject> {

    private String goodsid;
    private String state;

    public ChangeGoodsRequester(String goodsid,String state, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.goodsid = goodsid;
        this.state = state;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/changeState");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("goodsid", goodsid);
        params.put("state", state);
    }
}