package com.qckj.dabei.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;


/**
 * 标题栏控件
 * <p>
 * Created by yangzhizhong on 2019/3/19.
 */
public class ActionBar extends RelativeLayout implements View.OnClickListener {

    // 左边的按钮
    public static final int FUNCTION_BUTTON_LEFT = 1;

    // 右边的按钮
    public static final int FUNCTION_BUTTON_RIGHT = 2;

    // 中间的文本控件
    public static final int FUNCTION_TEXT_TITLE = 4;

    // 右边的文本控件
    public static final int FUNCTION_TEXT_RIGHT = 8;

    @FindViewById(R.id.action_bar_left_btn)
    private ImageView mLeftBtn;

    @FindViewById(R.id.action_bar_right_btn)
    public ImageView mRightBtn;

    @FindViewById(R.id.action_bar_title)
    private TextView mTitle;

    @FindViewById(R.id.action_bar_right_title)
    private TextView mRightTitle;

    // 默认打开的功能
    private int currentFunction = 0;

    private OnActionBarClickListener onActionBarClickListener;

    public void setOnActionBarClickListener(OnActionBarClickListener onActionBarClickListener) {
        this.onActionBarClickListener = onActionBarClickListener;
    }

    public ActionBar(Context context) {
        this(context, null);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView(context);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        // init
        Drawable background = typedArray.getDrawable(R.styleable.ActionBar_mBackground);
        if (background == null) {
            background = new ColorDrawable(getResources().getColor(R.color.transparent));
        }
        setBackground(background);
        setFunction(typedArray.getInt(R.styleable.ActionBar_function, 0));
        setTitleText(typedArray.getString(R.styleable.ActionBar_text_title));
        setRightText(typedArray.getString(R.styleable.ActionBar_text_right));
        setRightSrc(typedArray.getDrawable(R.styleable.ActionBar_srcRight));
        setLeftSrc(typedArray.getDrawable(R.styleable.ActionBar_srcLeft));
        typedArray.recycle();
    }

    public void setRightText(String rightText) {
        mRightTitle.setText(rightText);
    }
    public String getRightText() { return mRightTitle.getText().toString(); }

    private void setLeftSrc(Drawable drawable) {
        mLeftBtn.setImageDrawable(drawable);
    }

    private void setRightSrc(Drawable drawable) {
        mRightBtn.setImageDrawable(drawable);
    }

    public void setTitleText(String titleText) {
        mTitle.setText(titleText);
    }

    public void setRightBtnShow(boolean visible) {
       mRightBtn.setVisibility(visible?VISIBLE:GONE);
    }

    public boolean isAddFunction(int function) {
        return (this.currentFunction & function) == function;
    }

    public void addFunction(int function) {
        setFunction(this.currentFunction | function);
    }

    public void removeFunction(int function) {
        setFunction(this.currentFunction & (~function));
    }

    // 设置功能
    private void setFunction(int function) {
        if (this.currentFunction == function) {
            return;
        }
        this.currentFunction = function;
        mTitle.setVisibility(isAddFunction(FUNCTION_TEXT_TITLE) ? VISIBLE : INVISIBLE);
        // 添加leftButton
        mLeftBtn.setVisibility(isAddFunction(FUNCTION_BUTTON_LEFT) ? VISIBLE : INVISIBLE);
        // 添加RightButton
        mRightBtn.setVisibility(isAddFunction(FUNCTION_BUTTON_RIGHT) ? VISIBLE : INVISIBLE);
        mRightTitle.setVisibility(isAddFunction(FUNCTION_TEXT_RIGHT) ? VISIBLE : GONE);
    }

    private void createView(Context context) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.action_bar_view, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mRightTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int function = 0;
        if (v == mLeftBtn) {
            function = FUNCTION_BUTTON_LEFT;
        } else if (v == mRightBtn) {
            function = FUNCTION_BUTTON_RIGHT;
        } else if (v == mRightTitle) {
            function = FUNCTION_TEXT_RIGHT;
        }

        // 根据控件ID处理点击事件
        if (isAddFunction(function) && function != 0) {
            boolean result = false;
            if (onActionBarClickListener != null) {
                result = onActionBarClickListener.onActionBarClick(function);
            }

            // 只对返回按钮做处理
            if (!result) {
                Context context = getContext();
                if (context instanceof Activity) {
                    switch (function) {
                        case FUNCTION_BUTTON_LEFT:
                            hideSoftPad((Activity) context);
                            if (context instanceof BaseActivity) {
                                ((BaseActivity) context).onBackClick(mLeftBtn);
                            } else {
                                ((Activity) context).finish();
                            }
                            break;
                    }
                }
            }
        }

    }

    private void hideSoftPad(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert mInputMethodManager != null;
            mInputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 标题栏的点击事件
     */
    public interface OnActionBarClickListener {
        // 返回true 表示做处理，执行自己的事件， false 表示不做处理
        boolean onActionBarClick(int function);
    }
}
