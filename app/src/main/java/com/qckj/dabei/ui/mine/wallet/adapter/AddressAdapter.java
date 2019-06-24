package com.qckj.dabei.ui.mine.wallet.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.app.http.OnHttpResponseCodeListener;
import com.qckj.dabei.manager.mine.wallet.RemoveAddressRequester;
import com.qckj.dabei.manager.mine.wallet.RemoveRecordRequester;
import com.qckj.dabei.model.mine.DeliveryAddressInfo;
import com.qckj.dabei.model.mine.GoodsInfo;
import com.qckj.dabei.ui.mine.wallet.ChangeRecordActivity;
import com.qckj.dabei.ui.mine.wallet.DeliveryAddressActivity;
import com.qckj.dabei.ui.mine.wallet.EditAddressActivity;
import com.qckj.dabei.ui.mine.wallet.MineWalletActivity;
import com.qckj.dabei.ui.mine.wallet.OrderActivity;
import com.qckj.dabei.util.inject.FindViewById;

import org.json.JSONObject;

/**
 * Created by yangzhizhong on 2019/5/29.
 */
public class AddressAdapter extends SimpleBaseAdapter<DeliveryAddressInfo,AddressAdapter.ViewHolder> {
    public AddressAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_address;
    }

    @Override
    protected void bindView(ViewHolder holder, DeliveryAddressInfo deliveryAddressInfo, int position) {
        holder.textName.setText(deliveryAddressInfo.getName()+"(收)");
        holder.textPhone.setText(deliveryAddressInfo.getPhone());
        holder.textAddress.setText(deliveryAddressInfo.getCity()+deliveryAddressInfo.getAddress());
        holder.textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditAddressActivity.class);
                intent.putExtra("deliveryAddressInfo",deliveryAddressInfo);
                context.startActivity(intent);
            }
        });
        holder.textdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("是否确定删除该地址?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new RemoveAddressRequester(DeliveryAddressActivity.userId,deliveryAddressInfo.getId(),new OnHttpResponseCodeListener<JSONObject>(){
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
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.text_name)
        TextView textName;

        @FindViewById(R.id.text_phone)
        TextView textPhone;

        @FindViewById(R.id.text_address)
        TextView textAddress;

        @FindViewById(R.id.text_edit)
        TextView textEdit;

        @FindViewById(R.id.text_delete)
        TextView textdelete;
    }

}
