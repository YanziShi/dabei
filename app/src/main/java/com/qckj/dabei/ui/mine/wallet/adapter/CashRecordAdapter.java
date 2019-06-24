package com.qckj.dabei.ui.mine.wallet.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.SystemConfig;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.wallet.ConfirmOrderRequester;
import com.qckj.dabei.manager.mine.wallet.RemoveRecordRequester;
import com.qckj.dabei.model.mine.CashRecordInfo;
import com.qckj.dabei.model.mine.ChangeRecordInfo;
import com.qckj.dabei.ui.mine.wallet.ChangeRecordActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.view.webview.BrowserActivity;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/28.
 */
public class CashRecordAdapter extends SimpleBaseAdapter<CashRecordInfo, CashRecordAdapter.ViewHolder> {

    public CashRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_cash_record;
    }

    @Override
    protected void bindView(CashRecordAdapter.ViewHolder viewHolder, CashRecordInfo recordInfo, int position) {
        viewHolder.textNum.setText(recordInfo.getMoney());
        viewHolder.textTime.setText(recordInfo.getCtime());
        //0=待发货；1=已发货；2=已完成
        switch (recordInfo.getState()){
            case 1:
                viewHolder.textStatus.setText("审核中");
                break;
            case 2:
                viewHolder.textStatus.setText("提现中");
                break;
            case 3:
                viewHolder.textStatus.setText("提现完成");
                break;
            case 4:
                viewHolder.textStatus.setText("提现驳回");
                break;
        }



    }

    @NonNull
    @Override
    protected CashRecordAdapter.ViewHolder onNewViewHolder() {
        return new CashRecordAdapter.ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.text_num)
        private TextView textNum;

        @FindViewById(R.id.text_time)
        private TextView textTime;

        @FindViewById(R.id.text_status)
        private TextView textStatus;

    }
}