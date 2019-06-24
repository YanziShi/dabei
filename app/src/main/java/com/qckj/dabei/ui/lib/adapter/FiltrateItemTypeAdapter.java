package com.qckj.dabei.ui.lib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 筛选适配器
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class FiltrateItemTypeAdapter extends SimpleBaseAdapter<HomeFunctionInfo, FiltrateItemTypeAdapter.ViewHolder> {

    public FiltrateItemTypeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demand_lib_filtrate_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, HomeFunctionInfo info, int position) {
        viewHolder.itemName.setText(info.getName());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.item_name)
        private TextView itemName;
    }
}
