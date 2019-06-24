package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/8.
 */
public class GetMoreBrandPartnerRequester extends SimpleBaseRequester<List<HomeBrandPartnerInfo>> {

    private int rows;
    private int page;
    private String addrestwo;
    private String addresthree;

    public GetMoreBrandPartnerRequester(int rows,
                                        int page,
                                        String addrestwo,
                                        String addresthree,
                                        OnHttpResponseCodeListener<List<HomeBrandPartnerInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.rows = rows;
        this.page = page;
        this.addrestwo = addrestwo;
        this.addresthree = addresthree;
    }

    @Override
    protected List<HomeBrandPartnerInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"),HomeBrandPartnerInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/partnerlistpage");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("rows",rows);
        params.put("page",page);
        params.put("addrestwo",addrestwo);
        params.put("addresthree",addresthree);
    }
}