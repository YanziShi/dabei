package com.qckj.dabei.ui.mine.friend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.mine.InviteFriendInfo;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/5/31.
 */
public class FriendAdapter extends SimpleBaseAdapter<InviteFriendInfo.FriendInfo,FriendAdapter.ViewHolder> {
    public FriendAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_friend_view;
    }

    @Override
    protected void bindView(FriendAdapter.ViewHolder viewHolder, InviteFriendInfo.FriendInfo friendInfo, int position) {
        if(friendInfo==null) return;
        if(friendInfo.getType()==1) viewHolder.textType.setText("直接用户");
        else  viewHolder.textType.setText("间接用户");
        viewHolder.textAccount.setText(friendInfo.getAccount());
        viewHolder.textGrade.setText(friendInfo.getGradeName());
        viewHolder.textProfit.setText(friendInfo.getProfit());
    }

    @NonNull
    @Override
    protected FriendAdapter.ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    class ViewHolder{
        @FindViewById(R.id.text_type)
        TextView textType;
        @FindViewById(R.id.text_account)
        TextView textAccount;
        @FindViewById(R.id.text_grade)
        TextView textGrade;
        @FindViewById(R.id.text_profit)
        TextView textProfit;
    }
}
