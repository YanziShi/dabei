package com.qckj.dabei.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.district.DistrictItem;
import com.qckj.dabei.R;

import java.util.List;

/**
 * Created by zhguojiang on 2018/11/10.
 *
 */

public class CityAdapter extends BaseAdapter {

    private Context mContext;

    private List<DistrictItem>locationList;

    public CityAdapter(Context mContext, List<DistrictItem> locationList) {
        this.mContext = mContext;
        this.locationList = locationList;
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public Object getItem(int i) {
        return locationList.get(i);
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

            view= LayoutInflater.from(mContext).inflate(R.layout.item_location,null);
            viewHolder.location=view.findViewById(R.id.tvLocation);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.location.setText(locationList.get(i).getName());

        return view;
    }

    private static class ViewHolder {

        TextView location;

    }
}
