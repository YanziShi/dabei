package com.qckj.dabei.ui.mine.wallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.BankCardInfo;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.ui.mine.msg.adapter.MessageAdapter;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.view.image.CircleImageView;

/**
 * Created by yangzhizhong on 2019/5/27.
 */
public class BankCardAdapter extends SimpleBaseAdapter<BankCardInfo, BankCardAdapter.ViewHolder> {

    public BankCardAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_bank_card;
    }

    @Override
    protected void bindView(BankCardAdapter.ViewHolder viewHolder, BankCardInfo bankCardInfo, int position) {
        Glide.with(getContext()).load(bankCardInfo.getBankBg()).into(viewHolder.imageBg);
        Glide.with(getContext()).load(bankCardInfo.getBankImge()).into(viewHolder.imageHead);
        viewHolder.textName.setText(bankCardInfo.getName());
        viewHolder.textBankNum.setText(bankCardInfo.getBankNumber().substring(bankCardInfo.getBankNumber().length()-3));

    }

    @NonNull
    @Override
    protected BankCardAdapter.ViewHolder onNewViewHolder() {
        return new BankCardAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_bg)
        ImageView imageBg;

        @FindViewById(R.id.image_head)
        ImageView imageHead;

        @FindViewById(R.id.text_name)
        TextView textName;

        @FindViewById(R.id.text_bank_num)
        TextView textBankNum;

    }
}
