package com.qckj.dabei.app;

import com.qckj.dabei.util.log.Logger;

/**
 * 管理器基类，所有管理器都必须继承自本类
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public abstract class BaseManager {

    /** 日志打印， logger.d("a = %d", 3); */
    protected Logger logger = new Logger(getClass().getSimpleName());

    /** 管理器被初始化的回调，初始化整个管理器 */
    public abstract void onManagerCreate(App app);

    /** 所有管理器都初始化后执行 */
    public void onAllManagerCreated() {
    }

    /** 获得应用程序实例 */
    public App getApplication() {
        return App.getInstance();
    }

    /**
     * 获得管理器
     *
     * @param manager 管理器类型
     * @param <M>     管理器Class
     * @return 管理器
     */
    public <M extends BaseManager> M getManager(Class<M> manager) {
        return App.getInstance().getManager(manager);
    }
}
