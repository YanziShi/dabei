package com.qckj.dabei.manager.mine.complain;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/4/12.
 */
public class SubmitSuggestionRequester extends SimpleBaseRequester<Void> {

    private String role;
    private String message;
    private String captionUrl;
    private String userId;

    public SubmitSuggestionRequester( String role, String message, String captionUrl, String userId,OnHttpResponseCodeListener<Void> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.role = role;
        this.message = message;
        this.captionUrl = captionUrl;
        this.userId = userId;
    }

    @Override
    protected Void onDumpData(JSONObject jsonObject) {
        return null;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/userComplaint");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("role",role);
        params.put("message",message);
        params.put("caption_url",captionUrl);
        params.put("user_id",userId);
    }
}
