package com.qckj.dabei.manager.home;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.model.home.SearchAllInfo;
import com.qckj.dabei.util.json.JsonHelper;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/6.
 */
public class SearchRequester extends SimpleBaseRequester<List<SearchAllInfo>> {

    private String select_Text;
    private int select_Type;
    private int page;
    private int rows;
    private double longitude;
    private double latitude;

    public SearchRequester(String select_Text, int select_Type, int page, int rows, double longitude, double latitude,
                           OnHttpResponseCodeListener<List<SearchAllInfo>> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.select_Text = select_Text;
        this.select_Type = select_Type;
        this.page = page;
        this.rows = rows;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    protected List<SearchAllInfo> onDumpData(JSONObject jsonObject) {
        return JsonHelper.toList(jsonObject.optJSONArray("data"), SearchAllInfo.class);
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/selectAllByPaging_AppAllMes");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("select_Text", select_Text);
        params.put("select_Type", select_Type);
        params.put("user_y", longitude);
        params.put("user_x", latitude);
        params.put("rows", rows);
        params.put("page", page);
    }
}
