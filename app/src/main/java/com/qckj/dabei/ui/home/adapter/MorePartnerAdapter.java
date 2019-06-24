package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * @name: 梁永胜
 * @date ：2018/10/31 14:57
 * E-Mail Address：875450820@qq.com
 */
public class MorePartnerAdapter extends SimpleBaseAdapter<HomeBrandPartnerInfo, MorePartnerAdapter.ViewHolder> {

    public MorePartnerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_more_partner;
    }

    @Override
    protected void bindView(MorePartnerAdapter.ViewHolder viewHolder, HomeBrandPartnerInfo data, int position) {
        Glide.with(context).load(data.getImgUrl()).into(viewHolder.imageIcon);
        viewHolder.textName.setText(data.getName());
        viewHolder.textContent.setText(data.getDetail());

    }

    @NonNull
    @Override
    protected MorePartnerAdapter.ViewHolder onNewViewHolder() {
        return new MorePartnerAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_icon)
        private ImageView imageIcon;

        @FindViewById(R.id.tv_name)
        private TextView textName;

        @FindViewById(R.id.tv_content)
        private TextView textContent;

    }
}

