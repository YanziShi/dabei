package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/5/9.
 */
public class MoreBusinessActAdapter extends SimpleBaseAdapter<HomeBoutiqueRecommendInfo, MoreBusinessActAdapter.ViewHolder> {

    public MoreBusinessActAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_more_business_act;
    }

    @Override
    protected void bindView(MoreBusinessActAdapter.ViewHolder viewHolder, HomeBoutiqueRecommendInfo data, int position) {
        Glide.with(context).load(data.getPhoto()).into(viewHolder.imageIcon);
        viewHolder.textName.setText(data.getTitle());
        viewHolder.textContent.setText(data.getIntroduce());
        viewHolder.textTime.setText(data.getStartTime()+"-"+data.getEndTime());
    }

    @NonNull
    @Override
    protected MoreBusinessActAdapter.ViewHolder onNewViewHolder() {
        return new MoreBusinessActAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_icon)
        private ImageView imageIcon;

        @FindViewById(R.id.tv_name)
        private TextView textName;

        @FindViewById(R.id.tv_content)
        private TextView textContent;

        @FindViewById(R.id.tv_time)
        private TextView textTime;

    }
}


