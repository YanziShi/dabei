package com.qckj.dabei.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.qckj.dabei.R;

/**
 * 正在加载提示框
 * <p>
 * Created by yangzhizhong on 2019/3/21.
 */
public class LoadingProgressDialog extends Dialog {

    public LoadingProgressDialog(@NonNull Context context) {
        super(context, R.style.LoadingProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading_progress);
        if (getWindow() != null) {
            getWindow().setGravity(Gravity.CENTER);
        }
        setCancelable(false);
    }
}
