package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class CreateGoodOrderRequester extends SimpleBaseRequester<JSONObject> {
    String bb_price;
    String balance_price;
    int goods_number;
    String goods_id;
    String address_id;
    String userId;

    public CreateGoodOrderRequester(String userId,String bb_price,String balance_price,int goods_number,
            String goods_id,String address_id, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.balance_price = balance_price;
        this.bb_price = bb_price;
        this.goods_number = goods_number;
        this.goods_id = goods_id;
        this.address_id = address_id;

    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject.optJSONObject("data");
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/createGoodOrder");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("bb_price", bb_price);
        params.put("balance_price", balance_price);
        params.put("goods_number", goods_number);
        params.put("goods_id", goods_id);
        params.put("address_id", address_id);
    }
}
