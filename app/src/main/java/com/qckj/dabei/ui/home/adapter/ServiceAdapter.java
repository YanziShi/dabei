package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.RecommendServiceInfo;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * 服务适配器
 * <p>
 * Created by yangzhizhong on 2019/4/8.
 */
public class ServiceAdapter extends SimpleBaseAdapter<RecommendServiceInfo, ServiceAdapter.ViewHolder> {

    public ServiceAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recommend_service_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, RecommendServiceInfo serviceInfo, int position) {
        GlideUtil.displayImage(getContext(), serviceInfo.getImgUrl(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.name.setText(serviceInfo.getName());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.icon)
        private ImageView icon;

        @FindViewById(R.id.name)
        private TextView name;
    }
}
