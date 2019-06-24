package com.qckj.dabei.util;

import android.os.Handler;
import android.util.SparseArray;

/**
 * Created by yangzhizhong on 2019/4/15.
 */
public class AppData {

    private static SparseArray<Handler> ms_mapHandler = new SparseArray<Handler>();


    public static void addHandler(Integer a_iKey, Handler a_oHandler) {
        ms_mapHandler.put(a_iKey, a_oHandler);
    }

    public static void delHandler(Integer a_iKey) {
        if (ms_mapHandler.get(a_iKey) != null) {
            ms_mapHandler.remove(a_iKey);
        }
    }

}
