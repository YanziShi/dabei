package com.qckj.dabei.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

/**
 * 普通的条目控件
 * <p>
 * Created by yangzhizhong on 2019/3/20.
 */
public class CommonItemView extends LinearLayout {

    @FindViewById(R.id.title)
    private TextView mTitle;

    @FindViewById(R.id.content)
    private TextView mContent;

    @FindViewById(R.id.arrow)
    private ImageView mImageView;

    @FindViewById(R.id.line)
    private  View mLine;

    public CommonItemView(Context context) {
        this(context, null);
    }

    public CommonItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(context, attrs);
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.common_view_item, this, false);
        this.addView(view);
        ViewInject.inject(this, view);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonItemView);
        setTitle(typedArray.getString(R.styleable.CommonItemView_itemTitle));
        setContent(typedArray.getString(R.styleable.CommonItemView_itemContent));
        setArrowVisible(typedArray.getBoolean(R.styleable.CommonItemView_arrowVisible, true));
        setLineVisible(typedArray.getBoolean(R.styleable.CommonItemView_lineVisible, true));
        typedArray.recycle();
    }

    public void setContent(String content) {
        mContent.setText(content);
    }

    private void setLineVisible(boolean isVisible) {
        mLine.setVisibility(isVisible ? VISIBLE : GONE);
    }

    private void setArrowVisible(boolean isVisible) {
        mImageView.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
}
