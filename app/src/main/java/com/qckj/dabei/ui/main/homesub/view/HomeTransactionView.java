package com.qckj.dabei.ui.main.homesub.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeTransactionInfo;
import com.qckj.dabei.util.DateUtils;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;
import com.qckj.dabei.view.RollingView;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易信息的view
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeTransactionView extends LinearLayout {

    @FindViewById(R.id.roll_view)
    private RollingView mRollView;

    private Context mContext;

    public void setRollDatas(List<HomeTransactionInfo> homeTransactionInfos) {
        List<String> infos = new ArrayList<>();
        for (HomeTransactionInfo transactionInfo : homeTransactionInfos) {
            String finishDate = DateUtils.getTimeStringByMillisecondsWithFormatString(transactionInfo.getFinishDate(), "yyyy-MM-dd");
            String rollHead = mContext.getString(R.string.home_transaction_info_tip_head, transactionInfo.getClientName());
            String rollContent = mContext.getString(R.string.home_transaction_info_tip_content, finishDate, transactionInfo.getContentName());
            infos.add(rollHead);
            infos.add(rollContent);
        }
        mRollView.setPageSize(2);
        mRollView.setDelayedDuration(3000);
        mRollView.setRollingText(infos);
        mRollView.resume();
    }

    public HomeTransactionView(Context context) {
        this(context, null);
    }

    public HomeTransactionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTransactionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_home_transaction, this, false);
        this.addView(rootView);
        ViewInject.inject(this, rootView);
    }

}
