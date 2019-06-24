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
 * Created by yangzhizhong on 2019/5/24.
 */
public class SystemMsgAdapter extends SimpleBaseAdapter<MessageInfo, SystemMsgAdapter.ViewHolder> {

    public SystemMsgAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_system_msg;
    }

    @Override
    protected void bindView(SystemMsgAdapter.ViewHolder viewHolder, MessageInfo messageInfo, int position) {
        viewHolder.textTitle.setText(messageInfo.getName());
        viewHolder.textContent.setText(messageInfo.getContent());
        viewHolder.textTime.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(messageInfo.getTime(), "yyyy-MM-dd HH:mm:ss"));

    }

    @NonNull
    @Override
    protected SystemMsgAdapter.ViewHolder onNewViewHolder() {
        return new SystemMsgAdapter.ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.text_title)
        private TextView textTitle;

        @FindViewById(R.id.text_content)
        private TextView textContent;

        @FindViewById(R.id.text_time)
        private TextView textTime;
    }
}
