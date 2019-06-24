package com.qckj.dabei.ui.main.homesub;

import android.content.Context;
import android.view.View;

/**
 * 首页功能视图的基类
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public abstract class HomeBaseContent<Data> {

    private Data data;

    public HomeBaseContent(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public abstract View onCreateSubView(Context context);

    public abstract void onDestroySubView();

    public abstract void onRefreshView(Data data);
}
