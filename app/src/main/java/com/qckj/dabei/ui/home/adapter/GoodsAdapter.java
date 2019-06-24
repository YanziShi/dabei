package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.model.home.ServiceDetailInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class GoodsAdapter extends SimpleBaseAdapter<ServiceDetailInfo.GoodsBean, GoodsAdapter.ViewHolder> {
    @Manager
    private UserManager userManager;

    public GoodsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_goods;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, ServiceDetailInfo.GoodsBean goodsBean, int position) {
        GlideUtil.displayImage(getContext(), goodsBean.getF_C_SPFMIMG(), viewHolder.imageView, R.mipmap.ic_launcher);
        viewHolder.mTvPrice.setText(String.valueOf("￥ "+goodsBean.getF_I_MONEY()));
        viewHolder.mTvSellerName.setText(goodsBean.getF_C_SPNAME());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_icon)
        ImageView imageView;
        @FindViewById(R.id.tv_name)
        TextView mTvSellerName;
        @FindViewById(R.id.tv_price)
        TextView mTvPrice;
    }
}