package com.qckj.dabei.manager.release;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.release.DemandOrderInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.Map;

/**
 * 提交需求的请求
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class SubmitDemandRequester extends SimpleBaseRequester<DemandOrderInfo> {

    private String userId;
    private String classId;
    private String money;
    private String address;
    private String mes;
    private String imageUrl;
    private String phone;
    private double longitude;
    private double latitude;
    private String addressname;

    public SubmitDemandRequester(String userId, String classId, String money, String address, String mes,
                                 String imageUrl, String phone, double longitude, double latitude,String addressname,
                                 OnHttpResponseCodeListener<DemandOrderInfo> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.classId = classId;
        this.money = money;
        this.address = address;
        this.mes = mes;
        this.imageUrl = imageUrl;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.addressname = addressname;
    }

    @Override
    protected DemandOrderInfo onDumpData(JSONObject jsonObject) {
        return JsonHelper.toObject(jsonObject.optJSONObject("data"), DemandOrderInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/publishXuqiuByclassifytwoId");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("usid", userId);
        params.put("classifytwoId", classId);
        params.put("money", money);
        params.put("address", address);
        params.put("mes", mes);
        params.put("imgs", imageUrl);
        params.put("usphone", phone);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("addressname", addressname);
    }
}
