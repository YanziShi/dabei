package com.qckj.dabei.manager.mine.comment;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.comment.CommentInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 我的评论
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class GetCommentRequester extends SimpleBaseRequester<List<CommentInfo>> {

    private int page;
    private int pageSize;
    private String userId;

    public GetCommentRequester(int page, int pageSize, String userId, OnHttpResponseCodeListener<List<CommentInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.page = page;
        this.pageSize = pageSize;
        this.userId = userId;
    }

    @Override
    protected List<CommentInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), CommentInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/evaluate");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("page", page);
        params.put("rows", pageSize);
        params.put("userid", userId);
    }
}
