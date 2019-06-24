package com.qckj.dabei.util.inject;

import android.view.View;

import java.lang.reflect.Method;

/**
 * 点击事件注解
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class InjectOnClickListener implements View.OnClickListener {
    private Method method;
    private Object target;

    public InjectOnClickListener(Method method, Object target) {
        super();
        this.method = method;
        this.target = target;
    }

    @Override
    public void onClick(View v) {
        try {
            method.invoke(target, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
