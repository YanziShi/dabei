package com.qckj.dabei.app.http;

import android.os.Message;
import com.qckj.dabei.util.OMMap;
import java.util.List;

public class AppHandlerProxy {
    private static OMMap<Integer, AppHandler.HandlerMessageListener> m_handlerMessageListenerMap = new OMMap<Integer, AppHandler.HandlerMessageListener>();

    private static AppHandler.HandlerMessageListener handlerMessageListener = new AppHandler.HandlerMessageListener() {
        @Override
        public void handleMessage(Message msg) {
            List<AppHandler.HandlerMessageListener> handlerMessageListeners = m_handlerMessageListenerMap.get(msg.what);
            if (handlerMessageListeners != null) {
                for (AppHandler.HandlerMessageListener handlerMessageListener : handlerMessageListeners) {
                    handlerMessageListener.handleMessage(msg);
                }
            }
        }
    };
    private static AppHandler m_ipHandler = new AppHandler(handlerMessageListener);

    /** 该类不允许拥有实例 */
    private AppHandlerProxy() {

    }

    public static void postDelayed(Runnable runnable, int delayMillis) {
        m_ipHandler.postDelayed(runnable, delayMillis);
    }
}
