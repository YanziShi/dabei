package com.qckj.dabei.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.util.CommonUtils;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;

/**
 * 选择性别的对话框
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class SelectSexDialog {
    private Context context;
    private Dialog dialog;

    private OnSelectSexListener onSelectSexListener;

    public void setOnSelectSexListener(OnSelectSexListener onSelectSexListener) {
        this.onSelectSexListener = onSelectSexListener;
    }

    public SelectSexDialog(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        dialog = new Dialog(context, R.style.SelectDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_select_sex, null);
        ViewInject.inject(this, contentView);
        dialog.setContentView(contentView);
        dialog.setCancelable(false);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels - CommonUtils.dipToPx(context, 30);
        contentView.setLayoutParams(layoutParams);
    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        dialog.show();
    }

    @OnClick({R.id.select_sex_male, R.id.select_sex_female, R.id.dismiss})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.select_sex_male:
                dialog.dismiss();
                if (onSelectSexListener != null) {
                    onSelectSexListener.onSelectSex(true);
                }
                break;
            case R.id.select_sex_female:
                dialog.dismiss();
                if (onSelectSexListener != null) {
                    onSelectSexListener.onSelectSex(false);
                }
                break;
            case R.id.dismiss:
                dialog.dismiss();
                break;
        }
    }

    public interface OnSelectSexListener {
        void onSelectSex(boolean isMale);
    }
}
