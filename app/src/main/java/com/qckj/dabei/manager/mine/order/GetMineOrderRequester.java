package com.qckj.dabei.manager.mine.order;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取我的订单请求
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class GetMineOrderRequester extends SimpleBaseRequester<List<MineReleaseInfo>> {

    private int page;
    private int pageSize;
    private String userId;
    private int type;

    public GetMineOrderRequester(int page, int pageSize, String userId, int type, OnHttpResponseCodeListener<List<MineReleaseInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
        this.type = type;
    }

    @Override
    protected List<MineReleaseInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), MineReleaseInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/MyDemandList");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("userid", userId);
        params.put("type", type);
    }
}
