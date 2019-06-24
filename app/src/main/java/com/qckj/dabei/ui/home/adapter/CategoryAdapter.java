package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeFunctionInfo;

import java.util.List;

/**
 * Created by yangzhizhong on 2019/5/9.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mContext;

    private List<HomeFunctionInfo.Category> datas;

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<HomeFunctionInfo.Category> datas){
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

        CategoryAdapter.ViewHolder viewHolder;
        if (view==null){
            viewHolder=new CategoryAdapter.ViewHolder();

            view= LayoutInflater.from(mContext).inflate(R.layout.item_category,null);
            viewHolder.location=view.findViewById(R.id.text_name);
            view.setTag(viewHolder);
        }else {
            viewHolder= (CategoryAdapter.ViewHolder) view.getTag();
        }
        viewHolder.location.setText(datas.get(i).getClassName());
        return view;
    }

    private static class ViewHolder {
        TextView location;
    }
}
