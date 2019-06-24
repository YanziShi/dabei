package com.qckj.dabei.view.dialog;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.BaseActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.OnClick;
import com.qckj.dabei.util.inject.ViewInject;

import java.math.BigDecimal;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class ChoosePayTypeDialog extends Dialog{
    @FindViewById(R.id.dialog_article_share_touch_area)
    private View view;

    @FindViewById(R.id.dialog_article_share_touch_liner)
    private LinearLayout linearLayout;

    @FindViewById(R.id.text_title)
    private TextView titleTv;

    @FindViewById(R.id.text_balance)
    private TextView balanceTv;

    @FindViewById(R.id.text_deduction)
    private TextView deductionTv;

    @FindViewById(R.id.btn_balance)
    private TextView selectBalance;

    @FindViewById(R.id.btn_weixin)
    private TextView selectWeixin;

    @FindViewById(R.id.btn_aly)
    private TextView selectAly;

    @FindViewById(R.id.btn_deduction)
    private TextView selectDeduction;

    @FindViewById(R.id.frame_deduction)
    FrameLayout frameDeduction;

    @FindViewById(R.id.frame_balance)
    FrameLayout frameBalance;

    int type;
    double cost,price,balance;
    boolean isSelect;
    private Activity activity;

    public ChoosePayTypeDialog(Activity context) {
        super(context, R.style.Translucent);
        activity = context;
    }

    public void setData(double price,String unit, double balance) {
        this.price = price;
        this.balance = balance;
        this.cost = price;
        balanceTv.setText(String.valueOf(balance));
        if(unit!=null) titleTv.setText(String.valueOf(price)+"元/"+unit);
        else titleTv.setVisibility(View.GONE);
        if(balance<price) {
            type = 1;
            selectBalance.setSelected(false);
            selectAly.setSelected(false);
            selectWeixin.setSelected(true);
            selectDeduction.setSelected(true);
            cost = sub(price,balance);
            deductionTv.setText("余额抵扣"+String.valueOf(balance)+"元");
            frameDeduction.setVisibility(View.VISIBLE);
            frameBalance.setVisibility(View.GONE);
        }else{
            frameDeduction.setVisibility(View.GONE);
            frameBalance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_pay_type);
        ViewInject.inject(this);
        type = 0;
        selectBalance.setSelected(true);
        selectAly.setSelected(false);
        selectWeixin.setSelected(false);
    }

    @OnClick({R.id.btn_cancel,R.id.btn_sure,R.id.frame_balance,R.id.frame_weixin,R.id.frame_aly,R.id.frame_deduction})
    private void onViewClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                dismiss();
                if(listener!=null)listener.select(type,cost);
                break;
            case R.id.frame_balance:
                type = 0;
                selectBalance.setSelected(true);
                selectAly.setSelected(false);
                selectWeixin.setSelected(false);
                break;
            case R.id.frame_weixin:
                type = 1;
                selectBalance.setSelected(false);
                selectAly.setSelected(false);
                selectWeixin.setSelected(true);
                break;
            case R.id.frame_aly:
                type = 2;
                selectBalance.setSelected(false);
                selectAly.setSelected(true);
                selectWeixin.setSelected(false);
                break;
            case R.id.frame_deduction:
                isSelect = !isSelect;
                selectDeduction.setSelected(isSelect);
                if(isSelect) cost = sub(price,balance);
                else cost = price;
                break;
        }
    }

    public static Double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        Double doubleValue = b1.subtract(b2).doubleValue();
        return  doubleValue;
    }

    public interface  OnListener{ void select(int type,double cost);}
    private OnListener listener;
    public void setListener( OnListener listener){ this.listener = listener; }

    @Override
    public void show() {
        super.show();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(linearLayout, "translationY", BaseActivity.dipToPx(getContext(), 135), 0);
        objectAnimator.setDuration(300);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 0.8f);
        objectAnimator1.setDuration(300);
        objectAnimator1.start();
    }
}