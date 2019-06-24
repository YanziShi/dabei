package com.qckj.dabei.ui.mine.order.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 我的发布适配器
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineReleaseAdapter extends SimpleBaseAdapter<MineReleaseInfo, MineReleaseAdapter.ViewHolder> {

    public MineReleaseAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_release_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, MineReleaseInfo mineReleaseInfo, int position) {
        GlideUtil.displayImage(getContext(), mineReleaseInfo.getImage(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.count.setText(mineReleaseInfo.getCount() + "人参与");
        viewHolder.time.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(mineReleaseInfo.getTime(), "yyyy-MM-dd"));
        viewHolder.type.setText(mineReleaseInfo.getName());
        if ("1".equals(mineReleaseInfo.getState()) && "1".equals(mineReleaseInfo.getIsPay())) {
            viewHolder.payState.setText("已预付");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unprepaid_circlel));
        } else if ("2".equals(mineReleaseInfo.getState()) && "1".equals(mineReleaseInfo.getIsPay())) {
            viewHolder.payState.setText("等待付款");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_prepaid_circlel));
        } else {
            viewHolder.payState.setText("支付失败");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_prepaid_circlel));
        }
        viewHolder.money.setText("￥" + mineReleaseInfo.getMoney());
        switch (mineReleaseInfo.getState()) {
            case "0":
                viewHolder.orderState.setText("等待付款");
                viewHolder.orderState.setTextColor(Color.parseColor("#ff0000"));
                break;
            case "1":
                viewHolder.orderState.setText("接单中");
                viewHolder.orderState.setTextColor(Color.parseColor("#F79F00"));
                break;
            case "2":
                viewHolder.orderState.setText("完成接单");
                viewHolder.orderState.setTextColor(Color.parseColor("#00CC00"));
                break;
            case "3":
                viewHolder.orderState.setText("");
                break;
            case "4":
                viewHolder.orderState.setText("交易失败");
                viewHolder.orderState.setTextColor(Color.parseColor("#ff0000"));
                break;
            case "5":
                viewHolder.orderState.setText("等待接单");
                viewHolder.orderState.setTextColor(Color.parseColor("#13C2C2"));
                break;
        }
        if (position == getCount() - 1) {
            viewHolder.lineView.setVisibility(View.GONE);
        } else {
            viewHolder.lineView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private ImageView icon;

        @FindViewById(R.id.count)
        private TextView count;

        @FindViewById(R.id.time)
        private TextView time;

        @FindViewById(R.id.type)
        private TextView type;

        @FindViewById(R.id.pay_state)
        private TextView payState;

        @FindViewById(R.id.money)
        private TextView money;

        @FindViewById(R.id.order_state)
        private TextView orderState;

        @FindViewById(R.id.line_view)
        private View lineView;
    }

}
