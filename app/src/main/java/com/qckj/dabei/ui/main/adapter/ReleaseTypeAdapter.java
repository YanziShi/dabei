package com.qckj.dabei.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 发布类型
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class ReleaseTypeAdapter extends SimpleBaseAdapter<HomeFunctionInfo, ReleaseTypeAdapter.ViewHolder> {

    private int curPosition = 0;

    public ReleaseTypeAdapter(Context context) {
        super(context);
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.release_type_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, HomeFunctionInfo info, int position) {
        viewHolder.type.setText(info.getName());
        if (curPosition == position) {
            viewHolder.type.setSelected(true);
        } else {
            viewHolder.type.setSelected(false);
        }
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.type)
        private TextView type;
    }

}
