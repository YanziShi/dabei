package com.qckj.dabei.ui.mine.wallet.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.ui.mine.wallet.GoodsDetailActivity;
import com.qckj.dabei.ui.mine.wallet.MineWalletActivity;
import com.qckj.dabei.ui.mine.wallet.OrderActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;

/**
 * Created by yangzhizhong on 2019/5/24.
 */
public class GoodsAdapter extends SimpleBaseAdapter<GoodsInfo,GoodsAdapter.ViewHolder> {
    public GoodsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_goods_view;
    }

    @Override
    protected void bindView(ViewHolder holder, GoodsInfo goodsInfo, int position) {
        Glide.with(context).load(goodsInfo.getImageUrl()).into(holder.imageView);
        holder.textName.setText(goodsInfo.getName());
        holder.textOriginPrice.setText("原价:"+goodsInfo.getRmbPrice()+"元");
        holder.textSalePrice.setText("商城价:"+goodsInfo.getSaleRmbPrice()+"元+"+goodsInfo.getSaleBeizhuPrice()+"个贝珠");
        holder.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderActivity.startActivity((Activity) getContext(),goodsInfo);
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.image_view)
        ImageView imageView;

        @FindViewById(R.id.text_name)
        TextView textName;

        @FindViewById(R.id.text_origin_price)
        TextView textOriginPrice;

        @FindViewById(R.id.text_sale_price)
        TextView textSalePrice;

        @FindViewById(R.id.btn_change)
        Button btnChange;
    }

}