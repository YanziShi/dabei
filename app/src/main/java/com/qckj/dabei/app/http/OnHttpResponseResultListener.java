package com.qckj.dabei.app.http;

/**
 * HTTP请求结果 通用监听
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public interface OnHttpResponseResultListener<Data> extends ResultCode {

    /**
     * * 请求完成监听(网络错误)
     *
     * @param isSuccess 请求返回结果
     * @param data      请求的数据
     * @param message   错误信息
     */
    void onHttpResponse(boolean isSuccess, Data data, String message);

    /**
     * 没有连上服务器
     *
     * @param code 结果码
     */
    void onLocalErrorResponse(int code);
}
