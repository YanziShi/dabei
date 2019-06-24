package com.qckj.dabei.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qckj.dabei.R;

/**
 * @name: 梁永胜
 * @date ：2018/11/12 18:12
 * E-Mail Address：875450820@qq.com
 */
public class BottomDialog {
    private Context context;
    private Dialog bottomDialog;
    private TextView takePhoto;
    private TextView choosePhoto;
    public BottomDialog(Context context) {
        this.context = context;
    }

    public void show() {
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_content_normal, null);
        takePhoto = (TextView) contentView.findViewById(R.id.take_photo);
        choosePhoto = (TextView) contentView.findViewById(R.id.choose_photo);
        final TextView dismiss = (TextView) contentView.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    /**
     * 照相
     *
     * @param listener
     */
    public void setOnTakePhotoListener(View.OnClickListener listener) {
        takePhoto.setOnClickListener(listener);
    }
    /**
     * 相册
     *
     * @param listener
     */
    public void setOnChoosePhotoListener(View.OnClickListener listener) {
        choosePhoto.setOnClickListener(listener);
    }
    public void dismiss(){
        bottomDialog.dismiss();
    }
}
