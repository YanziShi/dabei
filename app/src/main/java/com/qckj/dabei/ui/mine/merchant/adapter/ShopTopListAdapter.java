package com.qckj.dabei.ui.mine.merchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.model.merchant.ShopTopInfo;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/20.
 */
public class ShopTopListAdapter extends BaseAdapter {

    private Context mContext;

    private List<ShopTopInfo> datas;

    public ShopTopListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ShopTopInfo> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas==null?null:datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();

            view= LayoutInflater.from(mContext).inflate(R.layout.item_shop_top_list,null);
            viewHolder.textTimePrice=view.findViewById(R.id.text_time_price);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textTimePrice.setText(datas.get(i).getTopPrice()+"元/"+datas.get(i).getTopTimeName());
        return view;
    }

    private static class ViewHolder {
        TextView textTimePrice;
    }
}
