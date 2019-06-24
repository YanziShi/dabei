package com.qckj.dabei.ui.mine.comment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.comment.CommentInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.GlideUtil;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.view.image.CircleImageView;

/**
 * 评论的实体类
 * <p>
 * Created by yangzhizhong on 2019/4/10.
 */
public class CommentAdapter extends SimpleBaseAdapter<CommentInfo, CommentAdapter.ViewHolder> {

    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mine_comment_item_view;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, CommentInfo commentInfo, int position) {
        GlideUtil.displayImage(context, commentInfo.getImageUrl(), viewHolder.icon, R.mipmap.ic_launcher);
        viewHolder.name.setText(commentInfo.getPhone());
        viewHolder.time.setText(DateUtils.getTimeStringByMillisecondsWithFormatString(commentInfo.getTime(), "yyyy-MM-dd"));
        viewHolder.content.setText(commentInfo.getMessage());
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.icon)
        private CircleImageView icon;

        @FindViewById(R.id.name)
        private TextView name;

        @FindViewById(R.id.time)
        private TextView time;

        @FindViewById(R.id.content)
        private TextView content;
    }
}
