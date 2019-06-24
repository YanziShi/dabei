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
public class InsertGoodsRequester extends SimpleBaseRequester<JSONObject> {

    String userId;
    String serviceName;
    double money;
    String serviceUnit;
    String serviceCover;
    String imgs;
    String message;
    String goodsid;

    public InsertGoodsRequester(String userId, String serviceName, double money,String serviceUnit,String serviceCover,
                                String imgs,String message,String goodsid,OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.serviceName = serviceName;
        this.money = money;
        this.serviceCover = serviceCover;
        this.serviceUnit = serviceUnit;
        this.imgs = imgs;
        this.message = message;
        this.goodsid = goodsid;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/insertGoods");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userId);
        params.put("serviceName", serviceName);
        params.put("money", money);
        params.put("serviceCover", serviceCover);
        params.put("serviceUnit", serviceUnit);
        params.put("imgs", imgs);
        params.put("message", message);
        params.put("goodsid", goodsid);
    }
}
