package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/5/9.
 */
public class TwoCategoryAdapter extends SimpleBaseAdapter<HomeFunctionInfo.Category, TwoCategoryAdapter.ViewHolder> {

    public TwoCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_more_category;
    }

    @Override
    protected void bindView(TwoCategoryAdapter.ViewHolder viewHolder, HomeFunctionInfo.Category mainTypeBean, int position) {
        viewHolder.textName.setText(mainTypeBean.getClassName());
        viewHolder.imageNext.setVisibility(View.VISIBLE);
        if(mainTypeBean.getIsSelected()) viewHolder.textName.setTextColor(Color.parseColor("#fece2f"));
        else viewHolder.textName.setTextColor(Color.parseColor("#000000"));
    }

    @NonNull
    @Override
    protected TwoCategoryAdapter.ViewHolder onNewViewHolder() {
        return new TwoCategoryAdapter.ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.tv_type_name)
        private TextView textName;
        @FindViewById(R.id.iv_next)
        private ImageView imageNext;

    }
}
