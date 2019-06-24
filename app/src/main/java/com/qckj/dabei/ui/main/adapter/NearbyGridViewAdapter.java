package com.qckj.dabei.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/3/26.
 */
public class NearbyGridViewAdapter extends SimpleBaseAdapter<HomeFunctionInfo.Category, NearbyGridViewAdapter.ViewHolder> {

    public NearbyGridViewAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.nearby_head_grid_view_item;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, HomeFunctionInfo.Category category, int position) {
        viewHolder.itemText.setText(category.getClassName());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    class ViewHolder {
        @FindViewById(R.id.item_text)
        private TextView itemText;
    }
}
