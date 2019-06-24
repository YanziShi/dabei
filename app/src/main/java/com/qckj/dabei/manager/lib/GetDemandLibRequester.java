package com.qckj.dabei.manager.lib;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.lib.DemandLibInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取需求库请求
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class GetDemandLibRequester extends SimpleBaseRequester<List<DemandLibInfo>> {

    private String id;
    private int page;
    private int pageSize;
    private String type;
    private String longitude;
    private String latitude;
    private String city;
    private String district;
    private String mold;
    private String user_id;

    public GetDemandLibRequester(String id, int page, int pageSize, String type, String longitude, String latitude,
                                 String city, String district,String mold,String user_id, OnHttpResponseCodeListener<List<DemandLibInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.id = id;
        this.page = page;
        this.pageSize = pageSize;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.city = city;
        this.district = district;
        this.mold = mold;
        this.user_id = user_id;
    }

    @Override
    protected List<DemandLibInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), DemandLibInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/get_demand");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        if (!TextUtils.isEmpty(id)) {
            params.put("id", id);
        }
        params.put("type", type);
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("user_y", longitude);
        params.put("user_x", latitude);
        params.put("addressname_two", city);
        params.put("addressname_three", district);
        params.put("mold", mold);
        params.put("user_id", user_id);
    }
}
