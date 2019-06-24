package com.qckj.dabei.ui.mine.order.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.model.mine.MineTripInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/5/23.
 */
public class MineTripAdapter extends SimpleBaseAdapter<MineTripInfo, MineTripAdapter.ViewHolder> {

    public MineTripAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_trip_item_view;
    }

    @Override
    protected void bindView(MineTripAdapter.ViewHolder viewHolder, MineTripInfo mineTripInfo, int position) {
        String strType = "";
        //custom(实时)book(预约)
        if (mineTripInfo.getTaskType().equals("custom")){
            strType = "(实时)";
            viewHolder.textStatus.setBackgroundColor(Color.parseColor("#F79F00"));
        }else{
            strType = "(预约)";
            viewHolder.textStatus.setBackgroundColor(Color.parseColor("#00cc00"));
        }
        //已发起/已接单/已取消/开始 /已完成
        //publish/accept/cancel/start/complete
        switch (mineTripInfo.getTaskState()){
            case "publish":
                viewHolder.textStatus.setText("已发起"+strType);
                break;
            case "accept":
                viewHolder.textStatus.setText("已接单"+strType);
                break;
            case "cancel":
                viewHolder.textStatus.setText("已取消"+strType);
                break;
            case "start":
                viewHolder.textStatus.setText("开始"+strType);
                break;
            case "complete":
                viewHolder.textStatus.setText("已完成"+strType);
                break;
        }
        viewHolder.textStart.setText(mineTripInfo.getOrigin());
        viewHolder.textEnd.setText(mineTripInfo.getDestination());
        viewHolder.textPrice.setText("指导价"+String.valueOf(mineTripInfo.getTotalFee())+"元");
        viewHolder.textDistance.setText("总里程"+String.valueOf(mineTripInfo.getDistance())+"km");
        viewHolder.textTime.setText(mineTripInfo.getFromTime());
    }

    @NonNull
    @Override
    protected MineTripAdapter.ViewHolder onNewViewHolder() {
        return new MineTripAdapter.ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.tv_status)
        private TextView textStatus;

        @FindViewById(R.id.tv_my_cf)
        private TextView textStart;

        @FindViewById(R.id.tv_my_md)
        private TextView textEnd;

        @FindViewById(R.id.tv_my_money)
        private TextView textPrice;

        @FindViewById(R.id.tv_distance)
        private TextView textDistance;

        @FindViewById(R.id.tv_time)
        private TextView textTime;
    }
}
