package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.model.merchant.MyServiceInfo;
import com.qckj.dabei.ui.mine.merchant.AddServiceActivity;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/5/17.
 */
public class MyServiceAdapter extends SimpleBaseAdapter<MyServiceInfo, MyServiceAdapter.ViewHolder> {

    Context context;

    public interface  OnListener{ void changeState(String goodsid,String state);}
    private OnListener listener;
    public void setListener( OnListener listener){ this.listener = listener; }

    public MyServiceAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_my_service;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, MyServiceInfo data, int position) {
        Glide.with(context).load(data.getServiceCover()).into(viewHolder.imageIcon);
        viewHolder.textName.setText(data.getServiceName());
        viewHolder.textPriceUnit.setText(data.getMoney()+"元/"+data.getServiceUnit());

        if(data.getState().equals("0")) {
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnAdd.setText("上架服务");
            viewHolder.textState.setText("未发布");
        } else if(data.getState().equals("1")) {
            viewHolder.btnDelete.setVisibility(View.GONE);
            viewHolder.textState.setText("已上架");
            viewHolder.btnAdd.setText("下架服务");
        } else if(data.getState().equals("2")) {
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnAdd.setText("上架服务");
            viewHolder.textState.setText("已下架");
        }

        viewHolder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddServiceActivity.startActivity(context,data);
            }
        });
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getState().equals("1")) changeState(data.getGoodsid(),"2");
                else changeState(data.getGoodsid(),"1");

            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(data.getGoodsid(),"-1");
            }
        });
    }

    void changeState(String goodsid,String state){
        if(listener!=null) listener.changeState(goodsid,state);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image_icon)
        private ImageView imageIcon;

        @FindViewById(R.id.text_name)
        private TextView textName;

        @FindViewById(R.id.text_price_unit)
        private TextView textPriceUnit;

        @FindViewById(R.id.text_state)
        private TextView textState;

        @FindViewById(R.id.btn_delete)
        private Button btnDelete;
        @FindViewById(R.id.btn_add)
        private Button btnAdd;
        @FindViewById(R.id.btn_modify)
        private Button btnModify;

    }
}