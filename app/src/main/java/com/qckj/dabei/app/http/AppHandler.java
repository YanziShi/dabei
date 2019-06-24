package com.qckj.dabei.app.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 对Handle封装
 *
 * @author zengdexing
 */
public class AppHandler extends Handler {
    private HandlerMessageListener mOnHandlerMessageListene;

    public AppHandler(HandlerMessageListener onHandlerMessageListener) {
        super(Looper.getMainLooper());
        this.mOnHandlerMessageListene = onHandlerMessageListener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (mOnHandlerMessageListene != null) {
            mOnHandlerMessageListene.handleMessage(msg);
        }
    }

    public interface HandlerMessageListener {
        void handleMessage(Message msg);
    }
}
