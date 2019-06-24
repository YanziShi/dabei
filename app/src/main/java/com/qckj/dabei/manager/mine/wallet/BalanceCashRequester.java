package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/28.
 */
public class BalanceCashRequester extends SimpleBaseRequester<JSONObject> {

    private String userId;
    private String cardId;
    private String money;

    public BalanceCashRequester(String userId, String cardId,String money, OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.cardId = cardId;
        this.money = money;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/addbalanceCash");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("card_id", cardId);
        params.put("money", money);
    }

}
