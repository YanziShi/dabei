package com.qckj.dabei.ui.release.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/6/1.
 */
public class SearchAddressAdapter extends SimpleBaseAdapter<PoiItem, SearchAddressAdapter.ViewHolder> {
    public SearchAddressAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_select_address;
    }

    @Override
    protected void bindView(ViewHolder holder, PoiItem poiItem, int position) {
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
    }

}
