package com.qckj.dabei.manager.mine.msg;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取消息
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class GetMessageRequester extends SimpleBaseRequester<List<MessageInfo>> {

    private int page;
    private int pageSize;
    private String userId;

    public GetMessageRequester(int page, int pageSize, String userId, OnHttpResponseCodeListener<List<MessageInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
    }

    @Override
    protected List<MessageInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), MessageInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/systemandchatMessage");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("userid", userId);
    }
}
