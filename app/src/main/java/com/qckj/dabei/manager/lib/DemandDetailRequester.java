package com.qckj.dabei.manager.lib;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.lib.DemandDetailInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/7.
 */
public class DemandDetailRequester extends SimpleBaseRequester<List<DemandDetailInfo>> {

        private String id;

        public DemandDetailRequester(String id, OnHttpResponseCodeListener<List<DemandDetailInfo>> onHttpResponseCodeListener) {
            super(onHttpResponseCodeListener);
            this.id = id;
        }

        @Override
        protected List<DemandDetailInfo> onDumpData(JSONObject jsonObject) {
            return JsonHelper.toList(jsonObject.optJSONArray("data"), DemandDetailInfo.class);
        }

        @NonNull
        @Override
        public String getServerUrl() {
            return SystemConfig.getServerUrl("/getXuqiuInformation");
        }

        @Override
        protected void onPutParams(Map<String, Object> params) {
            params.put("xuqiuId", id);
        }
}
