package com.qckj.dabei.app.http;

import android.widget.Toast;
import com.qckj.dabei.R;
import com.qckj.dabei.app.App;

/**
 * 结果码等封装
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class OnHttpResponseCodeListener<Data> implements OnHttpResponseResultListener<Data> {

    @Override
    public void onHttpResponse(boolean isSuccess, Data data, String message) {

    }

    @Override
    public void onLocalErrorResponse(int code) {
        if (code == RESULT_CODE_TIME_OUT) {
            Toast.makeText(App.getInstance(), R.string.toast_time_out, Toast.LENGTH_SHORT).show();
        } else if (code == RESULT_CODE_NET_ERROR) {
            Toast.makeText(App.getInstance(), R.string.toast_net_error, Toast.LENGTH_SHORT).show();
        } else {
            onServerCode();
        }
    }

    public void onServerCode() {
        Toast.makeText(App.getInstance(), R.string.toast_service_error, Toast.LENGTH_SHORT).show();
    }
}
