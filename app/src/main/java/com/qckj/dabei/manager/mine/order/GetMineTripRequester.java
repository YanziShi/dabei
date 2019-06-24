package com.qckj.dabei.manager.mine.order;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.model.mine.MineTripInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/23.
 */
public class GetMineTripRequester extends SimpleBaseRequester<List<MineTripInfo>> {

    private int page;
    private int pageSize;
    private String fromId;

    public GetMineTripRequester(int page, int pageSize, String fromId,  OnHttpResponseCodeListener<List<MineTripInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.page = page;
        this.pageSize = pageSize;
        this.fromId = fromId;
    }

    @Override
    protected List<MineTripInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), MineTripInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/store/androidPublishOrders");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("pageNo", page);
        params.put("pageSize", pageSize);
        params.put("fromId", fromId);
    }
}
