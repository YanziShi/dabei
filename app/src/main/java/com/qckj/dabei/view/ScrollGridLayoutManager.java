package com.qckj.dabei.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by yangzhizhong on 2019/3/23.
 */
public class ScrollGridLayoutManager extends GridLayoutManager {

    private boolean isScrollEnable = true;
    public ScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }
    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }
    /**
     * 设置 RecyclerView 是否可以垂直滑动
     *
     * @param isEnable true 可以
     */
    public void setScrollEnable(boolean isEnable) {
        this.isScrollEnable = isEnable;
    }
}
