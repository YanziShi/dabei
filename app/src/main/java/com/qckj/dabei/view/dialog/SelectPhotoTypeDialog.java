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
 * 选择头像方式的dialog
 * <p>
 * Created by yangzhizhong on 2019/3/28.
 */
public class SelectPhotoTypeDialog {
    private Context context;
    private Dialog dialog;

    private OnSelectPhotoListener onSelectPhotoListener;

    public void setOnSelectPhotoListener(OnSelectPhotoListener onSelectPhotoListener) {
        this.onSelectPhotoListener = onSelectPhotoListener;
    }

    public SelectPhotoTypeDialog(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        dialog = new Dialog(context, R.style.SelectDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_select_photo_type, null);
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

    @OnClick({R.id.take_photo, R.id.select_picture, R.id.dismiss})
    private void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.take_photo:
                dialog.dismiss();
                if (onSelectPhotoListener != null) {
                    onSelectPhotoListener.onSelectPhoto(true);
                }
                break;
            case R.id.select_picture:
                dialog.dismiss();
                if (onSelectPhotoListener != null) {
                    onSelectPhotoListener.onSelectPhoto(false);
                }
                break;
            case R.id.dismiss:
                dialog.dismiss();
                break;
        }
    }

    public interface OnSelectPhotoListener {
        void onSelectPhoto(boolean isTakePhoto);
    }
}
