package com.qckj.dabei.app.http;

/**
 * Created by yangzhizhong on 2019/3/21.
 */
public interface IResponseHandler {

    void onResult(int code, byte[] data);

    void onError(Exception exception);
}
