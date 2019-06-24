package com.qckj.dabei.app.http;

/**
 * 结果监听
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public interface OnResultMessageListener extends OnResult<String> {

    void onResult(boolean isSuccess, String message);
}
