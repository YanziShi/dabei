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
import com.qckj.dabei.manager.mine.user.UserManager;
import com.qckj.dabei.manager.mine.wallet.ConfirmOrderRequester;
import com.qckj.dabei.manager.mine.wallet.RemoveRecordRequester;
import com.qckj.dabei.model.mine.ChangeRecordInfo;
import com.qckj.dabei.model.mine.UserInfo;
import com.qckj.dabei.ui.mine.wallet.ChangeRecordActivity;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.Manager;
import com.qckj.dabei.view.webview.BrowserActivity;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/24.
 */
public class RecordAdapter extends SimpleBaseAdapter<ChangeRecordInfo, RecordAdapter.ViewHolder> {

    public RecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_change_record;
    }

    @Override
    protected void bindView(RecordAdapter.ViewHolder viewHolder, ChangeRecordInfo recordInfo, int position) {
        Glide.with(getContext()).load(recordInfo.getImageUrl()).into(viewHolder.imageView);
        viewHolder.textName.setText(recordInfo.getName());
        viewHolder.textOrigin.setText("原价:"+recordInfo.getRmbPrice()+"元");
        viewHolder.textSale.setText("商城价:"+recordInfo.getSaleRmbPrice()+"元+"+recordInfo.getSaleBeizhuPrice()+"个贝珠");
        viewHolder.textNumber.setText("数量:"+String.valueOf(recordInfo.getGoodsNumber()));
        //0=待发货；1=已发货；2=已完成
        switch (recordInfo.getOrderState()){
            case 0:
                viewHolder.textStatus.setText("待发货");
                viewHolder.btnLogistics.setVisibility(View.GONE);
                viewHolder.btnDelete.setVisibility(View.GONE);
                viewHolder.btnSure.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.textStatus.setText("已发货");
                viewHolder.btnLogistics.setVisibility(View.VISIBLE);
                viewHolder.btnDelete.setVisibility(View.GONE);
                viewHolder.btnSure.setVisibility(View.VISIBLE);
                break;
            case 2:
                viewHolder.textStatus.setText("已完成");
                viewHolder.btnLogistics.setVisibility(View.GONE);
                viewHolder.btnDelete.setVisibility(View.VISIBLE);
                viewHolder.btnSure.setVisibility(View.GONE);
                break;
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("确定删除该兑换记录?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveRecordRequester(ChangeRecordActivity.userId,recordInfo.getId(),new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                mListener.isChange();
                            }
                        }).doPost();
                    }
                });
                builder.setNegativeButton("取消",null );
                builder.show();
            }
        });

        viewHolder.btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("你确定已经收到货物？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ConfirmOrderRequester(ChangeRecordActivity.userId,recordInfo.getId(),new OnHttpResponseCodeListener<JSONObject>(){
                            @Override
                            public void onHttpResponse(boolean isSuccess, JSONObject jsonObject, String message) {
                                super.onHttpResponse(isSuccess, jsonObject, message);
                                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                mListener.isChange();
                            }
                        }).doPost();
                    }
                });
                builder.setNegativeButton("取消",null );
                builder.show();
            }
        });

        viewHolder.btnLogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = SystemConfig.webUrl+"/#/logistics?userId="+ ChangeRecordActivity.userId+"&orderId="+recordInfo.getId();
                BrowserActivity.startActivity(getContext(),url,false);
            }
        });

    }

    @NonNull
    @Override
    protected RecordAdapter.ViewHolder onNewViewHolder() {
        return new RecordAdapter.ViewHolder();
    }

    static class ViewHolder {

        @FindViewById(R.id.image_icon)
        private ImageView imageView;

        @FindViewById(R.id.tv_name)
        private TextView textName;

        @FindViewById(R.id.tv_origin)
        private TextView textOrigin;

        @FindViewById(R.id.tv_sale)
        private TextView textSale;

        @FindViewById(R.id.tv_status)
        private TextView textStatus;

        @FindViewById(R.id.tv_number)
        private TextView textNumber;

        @FindViewById(R.id.btn_delete)
        private Button btnDelete;

        @FindViewById(R.id.btn_sure)
        private Button btnSure;

        @FindViewById(R.id.btn_logistics)
        private Button btnLogistics;

    }
}
