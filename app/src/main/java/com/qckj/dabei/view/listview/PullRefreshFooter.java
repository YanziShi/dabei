package com.qckj.dabei.view.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.qckj.dabei.R;

/**
 * Created by yangzhizhong on 2019/3/22.
 */
public class PullRefreshFooter extends FrameLayout {

    private boolean isEnable = false;
    private View mFooter;

    public PullRefreshFooter(Context context) {
        super(context);
        mFooter = LayoutInflater.from(getContext()).inflate(R.layout.refresh_listview_footer, null);
        mFooter.setOnClickListener(view -> {

        });
        addView(mFooter);
        setEnabledLoadMore(isEnable);
    }

    public boolean isEnabledLoadMore() {
        return isEnable;
    }

    public void setEnabledLoadMore(boolean isEnable) {
        this.isEnable = isEnable;
        mFooter.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

}
