package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/24.
 */
public class GetGoodsRequester extends SimpleBaseRequester<List<GoodsInfo>> {

    private int page;
    private int pageSize;
    private String userId;

    public GetGoodsRequester(int page, int pageSize, String userId, OnHttpResponseCodeListener<List<GoodsInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
    }

    @Override
    protected List<GoodsInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), GoodsInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/store/getgoodspage");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("user_id", userId);
    }
}
