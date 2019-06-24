package com.qckj.dabei.ui.release.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 地址的适配器
 * @name: 梁永胜
 * @date ：2018/11/5 19:15
 * E-Mail Address：875450820@qq.com
 */

public class SelectAddressAdapter extends SimpleBaseAdapter<PoiItem, SelectAddressAdapter.ViewHolder> {
    public SelectAddressAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_select_address;
    }

    @Override
    protected void bindView(ViewHolder holder, PoiItem poiItem, int position) {
        if(position==0) holder.imageView.setVisibility(View.VISIBLE);
        else holder.imageView.setVisibility(View.INVISIBLE);
        holder.mTvTitle.setText(poiItem.getTitle());
        holder.mTvMessage.setText(poiItem.getSnippet());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.tv_title)
        TextView mTvTitle;
        @FindViewById(R.id.tv_message)
        TextView mTvMessage;
        @FindViewById(R.id.image_select)
        ImageView imageView;
    }

}
