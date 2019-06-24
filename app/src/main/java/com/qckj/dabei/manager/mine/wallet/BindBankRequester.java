package com.qckj.dabei.manager.mine.wallet;

import android.support.annotation.NonNull;

import com.qckj.dabei.app.SimpleBaseRequester;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class BindBankRequester extends SimpleBaseRequester<JSONObject> {

    String userId;
    String cardCode;
    String codeId;
    String code;
    String bankName;
    String abbrBank;
    String name;

    public BindBankRequester(String userId, String cardCode,String codeId, String code,String bankName,String abbrBank,String name,
                             OnHttpResponseCodeListener<JSONObject> onHttpResponseCodeListener) {
        super(onHttpResponseCodeListener);
        this.userId = userId;
        this.cardCode = cardCode;
        this.codeId =codeId;
        this.code = code;
        this.bankName = bankName;
        this.abbrBank = abbrBank;
        this.name = name;
    }

    @Override
    protected JSONObject onDumpData(JSONObject jsonObject) {
        return jsonObject;
    }

    @NonNull
    @Override
    public String getServerUrl() {
        return SystemConfig.getServerUrl("/my/putBlankCard");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("user_id", userId);
        params.put("card_code", cardCode);
        params.put("yzm_id", codeId);
        params.put("yzm", code);
        params.put("abbreviation", abbrBank);
        params.put("card_code", code);
        params.put("card_name", name);

    }

}
