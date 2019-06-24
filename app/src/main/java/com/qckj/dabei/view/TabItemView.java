package com.qckj.dabei.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

/**
 * tab条目
 * <p>
 * Created by yangzhizhong on 2019/3/22.
 */
public class TabItemView extends FrameLayout {

    // 图标
    private Drawable selectedIcon;
    //  图标 选中
    private Drawable normalIcon;
    @ColorInt
    private int normalColor;
    @ColorInt
    private int selectedColor;

    private Class<? extends Fragment> fragmentClass;

    private OnTabItemStateWillChangeListener onTabItemStateWillChangeListener;

    private boolean isItemSelected = false;
    @FindViewById(R.id.tab_item_icon)
    private ImageView itemIcon;
    @FindViewById(R.id.tab_item_text)
    private TextView itemText;

    public TabItemView(@NonNull Context context) {
        this(context, null);
    }

    public TabItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        initView();
        initAttrs(context, attrs);
        initListener();
    }

    private void initListener() {
        this.setOnClickListener(v -> {
            if (onTabItemStateWillChangeListener != null) {
                boolean shouldChange = onTabItemStateWillChangeListener.shouldChangeTabItemState(TabItemView.this);
                if (shouldChange) {
                    isItemSelected = !isItemSelected;
                    checkState();
                    onTabItemStateWillChangeListener.onTabItemStatChanged(TabItemView.this);
                }
            } else {
                isItemSelected = !isItemSelected;
                checkState();
            }
        });
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_tab_item, this, false);
        addView(view);
        ViewInject.inject(this, view);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabItemView);
        normalIcon = typedArray.getDrawable(R.styleable.TabItemView_itemIcon);
        selectedIcon = typedArray.getDrawable(R.styleable.TabItemView_itemIconSelected);
        setText(typedArray.getString(R.styleable.TabItemView_itemText));
        normalColor = typedArray.getColor(R.styleable.TabItemView_itemTextColor, Color.parseColor("#000000"));
        selectedColor = typedArray.getColor(R.styleable.TabItemView_itemTextColorSelected, Color.parseColor("#000000"));
        isItemSelected = typedArray.getBoolean(R.styleable.TabItemView_isItemSelected, false);
        checkState();
        typedArray.recycle();
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    private void setText(String text) {
        itemText.setText(text);
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void setItemSelected(boolean isItemSelected) {
        this.isItemSelected = isItemSelected;
        checkState();
    }

    public void setOnTabItemStateWillChangeListener(OnTabItemStateWillChangeListener onTabItemStateWillChangeListener) {
        this.onTabItemStateWillChangeListener = onTabItemStateWillChangeListener;
    }

    private void checkState() {
        if (isItemSelected) {
            itemText.setTextColor(selectedColor);
            itemIcon.setImageDrawable(selectedIcon);
        } else {
            itemText.setTextColor(normalColor);
            itemIcon.setImageDrawable(normalIcon);
        }
    }

    public interface OnTabItemStateWillChangeListener {
        boolean shouldChangeTabItemState(TabItemView tabItemView);

        void onTabItemStatChanged(TabItemView tabItemView);
    }

}
