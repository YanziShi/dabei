package com.qckj.dabei.app.http;

import java.io.UnsupportedEncodingException;

/**
 * Created by yangzhizhong on 2019/3/21.
 */
public abstract class StringResponseHandler implements IResponseHandler {
    @Override
    public void onResult(int code, byte[] data) {
        try {
            String string = new String(data, "UTF-8");
            onResult(code, string);
        } catch (UnsupportedEncodingException e) {
            onError(e);
        }
    }

    public abstract void onResult(int code, String content);
}
