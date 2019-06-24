package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qckj.dabei.R;
import com.qckj.dabei.model.street.BusinessCircleInfo;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/8.
 */
public class BusinessCircleAdapter extends BaseAdapter {

    private Context mContext;

    private List<BusinessCircleInfo> datas;

    public BusinessCircleAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<BusinessCircleInfo> datas){this.datas = datas;}

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

        BusinessCircleAdapter.ViewHolder viewHolder;
        if (view==null){
            viewHolder=new BusinessCircleAdapter.ViewHolder();

            view= LayoutInflater.from(mContext).inflate(R.layout.item_street,null);
            viewHolder.location=view.findViewById(R.id.text_name);
            view.setTag(viewHolder);
        }else {
            viewHolder= (BusinessCircleAdapter.ViewHolder) view.getTag();
        }
        viewHolder.location.setText(datas.get(i).getName());
        return view;
    }

    private static class ViewHolder {
        TextView location;
    }
}