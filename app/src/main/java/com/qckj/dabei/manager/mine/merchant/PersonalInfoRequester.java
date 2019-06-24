package com.qckj.dabei.manager.mine.merchant;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.merchant.MerchantInfo;
import com.qckj.dabei.model.merchant.PersonalInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/16.
 */
public class PersonalInfoRequester extends SimpleBaseRequester<PersonalInfo> {

    private String userId;
    private String city;
    private String district;

    public PersonalInfoRequester(String userId,String city, String district,OnHttpResponseCodeListener<PersonalInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.city = city;
        this.district = district;
    }

    @Override
    protected PersonalInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toObject(jsonObject.optJSONObject("data"), PersonalInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/getPersonalInformationById");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("addressname_two", city);
        params.put("addressname_three", district);
    }
}
