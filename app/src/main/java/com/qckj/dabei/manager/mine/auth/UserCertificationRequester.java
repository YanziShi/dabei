package com.qckj.dabei.manager.mine.auth;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/14.
 */
public class UserCertificationRequester extends SimpleBaseRequester<JSONObject> {

    private String userid;
    private String name;
    private String idcard;
    private String idcard_front;
    private String idcard_back;

    public UserCertificationRequester(String userid,String name, String idcard, String idcard_front,String idcard_back,
                                      OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userid = userid;
        this.name = name;
        this.idcard = idcard;
        this.idcard_front = idcard_front;
        this.idcard_back = idcard_back;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/addUserCertification");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("userid", userid);
        params.put("name", name);
        params.put("idcard", idcard);
        params.put("idcard_front", idcard_front);
        params.put("idcard_back", idcard_back);
    }
}