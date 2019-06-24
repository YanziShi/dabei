package com.qckj.dabei.manager.mine.order;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.mine.MineTripInfo;
import com.qckj.dabei.model.mine.ParticipantInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/6/1.
 */
public class ParticipantRequester extends SimpleBaseRequester<List<ParticipantInfo>> {

    private String xuqiuid;
    private String type;
    private String userid;

    public ParticipantRequester( String userid,String xuqiuid, String type,  OnHttpResponseCodeListener<List<ParticipantInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.xuqiuid = xuqiuid;
        this.type = type;
        this.userid = userid;
    }

    @Override
    protected List<ParticipantInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), ParticipantInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/orderDetail");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("xuqiuid", xuqiuid);
        params.put("type", type);
        params.put("userid", userid);
    }
}
