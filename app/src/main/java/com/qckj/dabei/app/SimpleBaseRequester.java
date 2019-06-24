package com.qckj.dabei.app;

import android.text.TextUtils;

import com.qckj.dabei.app.http.HttpRequester;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.app.http.ResultCode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 简单请求
 * 数据过多，请使用HttpRequester
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public abstract class SimpleBaseRequester<Data> extends HttpRequester {

    protected OnHttpResponseCodeListener<Data> onHttpResponseCodeListener;

    public SimpleBaseRequester(OnHttpResponseCodeListener<Data> onHttpResponseCodeListener) {
        this.onHttpResponseCodeListener = onHttpResponseCodeListener;
    }

    @Override
    protected void onResult(boolean isSuccess, JSONObject content) throws JSONException {
        Data data = null;
        if (isSuccess) {
            data = onDumpData(content);
        }
        onHttpResponseCodeListener.onHttpResponse(isSuccess, data, content == null ? "" :
                TextUtils.isEmpty(content.optString("mes")) ? content.optString("message") : content.optString("mes"));
    }

    protected abstract Data onDumpData(JSONObject jsonObject);

    @Override
    protected void onError(Exception e) {
        e.printStackTrace();
        onHttpResponseCodeListener.onLocalErrorResponse(ResultCode.RESULT_CODE_NET_ERROR);
    }
}
