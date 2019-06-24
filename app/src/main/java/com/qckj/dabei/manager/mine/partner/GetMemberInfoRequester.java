package com.qckj.dabei.manager.mine.partner;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.MemberInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 获取会员信息
 * <p>
 * Created by yangzhizhong on 2019/4/11.
 */
public class GetMemberInfoRequester extends SimpleBaseRequester<List<MemberInfo>> {

    private String userId;
    private int page;
    private int pageSize;

    public GetMemberInfoRequester(String userId, int page, int pageSize, OnHttpResponseCodeListener<List<MemberInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.page = page;
        this.pageSize = pageSize;
    }

    @Override
    protected List<MemberInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), MemberInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/getMemberList");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("rows", pageSize);
        params.put("page", page);
    }
}
