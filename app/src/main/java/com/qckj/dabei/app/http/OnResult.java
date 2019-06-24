package com.qckj.dabei.app.http;

/**
 * Created by yangzhizhong on 2019/3/28.
 */
public interface OnResult<Data> {
    void onResult(boolean isSuccess, Data data);
}
