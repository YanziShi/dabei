package com.qckj.dabei.view.listview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.util.CommonUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 下拉刷新头
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class PullRefreshHeader extends LinearLayout {
    /**
     * 正常状态
     */
    public static final int STATE_INIT = 0;
    /**
     * 即将下拉刷新
     */
    public static final int STATE_RELEASE_REFRESH = 1;

    /**
     * 正在刷新
     */
    public static final int STATE_REFRESHING = 2;

    private TextView mTextView;
    private View mContainer;

    private int pullToRefreshStringId = R.string.refresh_pull_to_refresh;
    private int releaseToRefreshStringId = R.string.refresh_release_to_refresh;
    private int refreshingStringId = R.string.refresh_refreshing;

    private int mState = STATE_INIT;

    private View offsetView;
    private int offsetHeight;

    public PullRefreshHeader(Context context) {
        super(context);
        initView();
    }

    public View getContainer() {
        return mContainer;
    }

    private void initView() {
        mContainer = LayoutInflater.from(getContext()).inflate(R.layout.refresh_listview_header, null);
        mTextView = mContainer.findViewById(R.id.refresh_header_textview);
        offsetView = mContainer.findViewById(R.id.refresh_header_offset_view);

        setGravity(Gravity.BOTTOM);
        this.addView(mContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));

        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing
            }
        });
    }

    private Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public void setState(int state) {
        if (state == mState)
            return;

        switch (state) {
            case STATE_INIT:
                mTextView.setText(pullToRefreshStringId);
                break;

            case STATE_RELEASE_REFRESH:
                mTextView.setText(releaseToRefreshStringId);
                break;

            case STATE_REFRESHING:
                mTextView.setText(refreshingStringId);
                break;
            default:
        }

        mState = state;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
        offsetView.getLayoutParams().height = offsetHeight;
    }

    public int getContentHeight() {
        return CommonUtils.dipToPx(getContext(), 60) + offsetHeight;
    }

    public int getVisibilityHeight() {
        return mContainer.getHeight();
    }

    public void setVisibilityHeight(int height) {
        if (height < 0)
            height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }
}

