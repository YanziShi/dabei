package com.qckj.dabei.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.qckj.dabei.R;

/**
 * @name: 梁永胜
 * @date ：2018/9/19 14:08
 * E-Mail Address：875450820@qq.com
 */
public class MsgDialog extends Dialog {
    private MsgDialog dialog;
    private TextView positiveButton;
    private TextView negativeButton;
    private TextView titleTv;
    private TextView cotentTv;

    public MsgDialog(Context context) {
        super(context);
        dialog = this;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去背景
        setMsgDialog();

    }
    public void show(String title,String content,String sure,boolean isContent){
        if (isContent){
            cotentTv.setText(content);
        }else {
            cotentTv.setVisibility(View.GONE);
        }
        titleTv.setText(title);
        positiveButton.setText(sure);
        dialog.show();
    }

    private void setMsgDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null);
        titleTv = (TextView) mView.findViewById(R.id.title);
        cotentTv = (TextView)mView.findViewById(R.id.content_tv);
        positiveButton = (TextView) mView.findViewById(R.id.ok);
        negativeButton = (TextView) mView.findViewById(R.id.cancle);
        if (positiveButton != null) positiveButton.setOnClickListener(listener);
        if (negativeButton != null) negativeButton.setOnClickListener(listener);
        super.setContentView(mView);
    }

    //点击之后消失
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MsgDialog.this.dismiss();
        }
    };

    /**
     * 确定键监听器
     *
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener) {
        positiveButton.setOnClickListener(listener);
    }

    /**
     * 取消键监听器
     *
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener) {
        negativeButton.setOnClickListener(listener);
    }

}
