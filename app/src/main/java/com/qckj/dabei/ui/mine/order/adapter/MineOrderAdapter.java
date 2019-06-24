package com.qckj.dabei.ui.mine.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.view.dialog.MsgDialog;
import com.qckj.dabei.model.mine.MineReleaseInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 我的订单信息适配器
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class MineOrderAdapter extends SimpleBaseAdapter<MineReleaseInfo, MineOrderAdapter.ViewHolder> {

    public MineOrderAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_order_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, MineReleaseInfo mineReleaseInfo, int position) {
        viewHolder.time.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(mineReleaseInfo.getTime(), "yyyy-MM-dd"));
        viewHolder.type.setText(mineReleaseInfo.getName());
        if ("1".equals(mineReleaseInfo.getState()) && "1".equals(mineReleaseInfo.getIsPay())) {
            viewHolder.payState.setText("已预付");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_unprepaid_circlel));
        } else if ("2".equals(mineReleaseInfo.getState()) && "1".equals(mineReleaseInfo.getIsPay())) {
            viewHolder.payState.setText("未预付");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_prepaid_circlel));
        } else {
            viewHolder.payState.setText("支付失败");
            viewHolder.payState.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.bg_prepaid_circlel));
        }
        viewHolder.content.setText(mineReleaseInfo.getMes());
        viewHolder.money.setText("￥" + mineReleaseInfo.getMoney());
        viewHolder.contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mineReleaseInfo.getPhone();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getContext(), "未预留电话", Toast.LENGTH_SHORT).show();
                } else {
                    showPhoneDialog(phone);
                }
            }
        });

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

    private void showPhoneDialog(String phone) {
        MsgDialog dialog = new MsgDialog(getContext());
        dialog.show(phone,"","拨打",false);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callPhone(phone);
            }
        });
    }

    private void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        getContext().startActivity(intent);
    }

    static class ViewHolder {

        @FindViewById(R.id.type)
        private TextView type;

        @FindViewById(R.id.pay_state)
        private TextView payState;

        @FindViewById(R.id.time)
        private TextView time;

        @FindViewById(R.id.content)
        private TextView content;

        @FindViewById(R.id.money)
        private TextView money;

        @FindViewById(R.id.contact_btn)
        private TextView contactBtn;

        @FindViewById(R.id.line_view)
        private View lineView;
    }
}
