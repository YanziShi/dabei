package com.qckj.dabei.util.inject;

import com.qckj.dabei.app.BaseManager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * 注解manager
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class ManagerInject {

    public static void inject(Object object, HashMap<String, BaseManager> managerList) {
        Class<?> clazz = object.getClass();
        while (clazz != null && clazz != BaseManager.class && clazz != Object.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            if (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                        // 忽略掉static 和 final 修饰的变量
                        continue;
                    }
                    if (!field.isAnnotationPresent(Manager.class)) {
                        continue;
                    }
                    Class<?> type = field.getType();
                    if (!BaseManager.class.isAssignableFrom(type)) {
                        throw new RuntimeException("@Manager 注解只能应用到BaseManager的子类");
                    }

                    BaseManager baseManager = managerList.get(type.getName());

                    if (baseManager == null) {
                        throw new RuntimeException(type.getSimpleName() + " 管理类还未初始化！");
                    }
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    try {
                        field.set(object, baseManager);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

}
