package com.qckj.dabei.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/29.
 */
public class HealthData {

    /**
     * 判断Intent转向的应用组件是否存在
     *
     * @param a_oContext
     * @param a_oIntent  隐式调用的Intent
     * @return
     */
    public static boolean isIntentExist(Context a_oContext, Intent a_oIntent) {
        boolean isExist = false;
        // PackageManager是用于判断Intent转向是否成功的管理器
        PackageManager packageManager = a_oContext.getPackageManager();
        List<ResolveInfo> componentList = packageManager.queryIntentActivities(a_oIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (componentList.size() > 0) {
            isExist = true;
        }

        return isExist;
    }
}
