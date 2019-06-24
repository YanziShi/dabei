package com.qckj.dabei.ui.mine.partner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.MemberInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.view.image.CircleImageView;

/**
 * 介入合伙人条目
 * <p>
 * Created by yangzhizhong on 2019/3/26.
 */
public class MemberAdapter extends SimpleBaseAdapter<MemberInfo,MemberAdapter.ViewHolder> {


    public MemberAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.member_item_view;
    }

    @Override
    protected void bindView(MemberAdapter.ViewHolder viewHolder, MemberInfo memberInfo, int position) {
        Glide.with(getContext()).load(memberInfo.getMemberLogo()).into(viewHolder.icon);
        viewHolder.title.setText(memberInfo.getMemberName()+"￥"+memberInfo.getMemberPrice()+"元");
        viewHolder.content.setText(memberInfo.getMemberIntroduce());
    }

    @NonNull
    @Override
    protected MemberAdapter.ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private ImageView icon;
        @FindViewById(R.id.title)
        private TextView title;
        @FindViewById(R.id.content)
        private TextView content;
    }
}
