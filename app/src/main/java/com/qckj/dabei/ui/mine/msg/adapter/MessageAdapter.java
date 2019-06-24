package com.qckj.dabei.ui.mine.msg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.MessageInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.view.image.CircleImageView;

/**
 * Created by yangzhizhong on 2019/4/8.
 */
public class MessageAdapter extends SimpleBaseAdapter<MessageInfo, MessageAdapter.ViewHolder> {

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.message_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, MessageInfo messageInfo, int position) {
        GlideUtil.displayImage(getContext(), messageInfo.getImageUrl(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.title.setText(messageInfo.getName());
        viewHolder.content.setText(messageInfo.getContent());
        viewHolder.time.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(messageInfo.getTime(), "yyyy-MM-dd"));
        if(messageInfo.getCount()>0) {
            viewHolder.textUnread.setText(String.valueOf(messageInfo.getCount()));
            viewHolder.textUnread.setVisibility(View.VISIBLE);
        }else viewHolder.textUnread.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.icon)
        private CircleImageView icon;

        @FindViewById(R.id.title)
        private TextView title;

        @FindViewById(R.id.content)
        private TextView content;

        @FindViewById(R.id.time)
        private TextView time;

        @FindViewById(R.id.text_unread)
        private TextView textUnread;
    }
}
