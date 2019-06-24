package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/30.
 */
public class EditAddressRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private String addressId;
    private String name;
    private String phone;
    private String city;
    private String address;

    public EditAddressRequester(String userId, String addressId, String name,String phone,String city,String address,
                                OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.addressId = addressId;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.address = address;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/addAress");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("receiver", name);
        params.put("address", address);
        params.put("sheng", city);
        params.put("phone", phone);
        params.put("address_id", addressId);

    }
}
