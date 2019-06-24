package com.qckj.dabei.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 发布项目适配器
 * <p>
 * Created by yangzhizhong on 2019/4/9.
 */
public class ReleaseProjectAdapter extends SimpleBaseAdapter<HomeFunctionInfo.Category, ReleaseProjectAdapter.ViewHolder> {

    public ReleaseProjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.release_project_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, HomeFunctionInfo.Category category, int position) {
        viewHolder.type.setText(category.getClassName());
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
