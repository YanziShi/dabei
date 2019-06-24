package com.qckj.dabei.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeFunctionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/26.
 */
public class NearbyTabListAdapter extends RecyclerView.Adapter<NearbyTabListAdapter.ViewHolder> {

    private Context context;
    private List<HomeFunctionInfo> infos;
    private int curPosition = 0;
    private OnTabListClickListener onTabListClickListener;

    public NearbyTabListAdapter(Context context) {
        this.context = context;
        infos = new ArrayList<>();
    }

    public void setInfos(List<HomeFunctionInfo> infos) {
        this.infos.clear();
        this.infos = infos;
        notifyDataSetChanged();
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        notifyDataSetChanged();
    }

    public void setOnTabListClickListener(OnTabListClickListener onTabListClickListener) {
        this.onTabListClickListener = onTabListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.nearby_head_tab_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeFunctionInfo homeFunctionInfo = infos.get(position);
        holder.itemText.setText(homeFunctionInfo.getName());
        if (position == curPosition) {
            holder.itemText.setSelected(true);
        } else {
            holder.itemText.setSelected(false);
        }
        holder.itemText.setOnClickListener(v -> {
            if (onTabListClickListener != null) {
                onTabListClickListener.onTabClick(position, homeFunctionInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.tab_item_text);
        }
    }

    public interface OnTabListClickListener {
        void onTabClick(int position, HomeFunctionInfo info);
    }

}
