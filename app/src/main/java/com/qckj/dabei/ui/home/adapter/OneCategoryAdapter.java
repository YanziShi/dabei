package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeFunctionInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * @name: 梁永胜
 * @date ：2018/11/5 19:15
 * E-Mail Address：875450820@qq.com
 */
public class OneCategoryAdapter extends SimpleBaseAdapter<HomeFunctionInfo, OneCategoryAdapter.ViewHolder> {

    public OneCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_more_category;
    }

    @Override
    protected void bindView(OneCategoryAdapter.ViewHolder viewHolder, HomeFunctionInfo mainTypeBean, int position) {
        viewHolder.textName.setText(mainTypeBean.getName());
        if(mainTypeBean.getIsSelected()) viewHolder.textName.setTextColor(Color.parseColor("#fece2f"));
        else viewHolder.textName.setTextColor(Color.parseColor("#000000"));
    }

    @NonNull
    @Override
    protected OneCategoryAdapter.ViewHolder onNewViewHolder() {
        return new OneCategoryAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.tv_type_name)
        private TextView textName;

    }
}