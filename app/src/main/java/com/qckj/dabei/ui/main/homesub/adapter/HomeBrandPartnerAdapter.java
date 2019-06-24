package com.qckj.dabei.ui.main.homesub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.model.home.HomeBrandPartnerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBrandPartnerAdapter extends RecyclerView.Adapter<HomeBrandPartnerAdapter.HomeBrandPartnerViewHolder> {

    private Context context;
    private List<HomeBrandPartnerInfo> homeBrandPartnerInfos;
    private OnItemClickListener onItemClickListener;

    public HomeBrandPartnerAdapter(Context context) {
        this.context = context;
        this.homeBrandPartnerInfos = new ArrayList<>();
    }

    public void setHomeBrandPartnerInfos(List<HomeBrandPartnerInfo> homeBrandPartnerInfos) {
        this.homeBrandPartnerInfos.clear();
        this.homeBrandPartnerInfos = homeBrandPartnerInfos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HomeBrandPartnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_brand_partner_item_view, parent, false);
        return new HomeBrandPartnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBrandPartnerViewHolder holder, int position) {
        HomeBrandPartnerInfo homeBrandPartnerInfo = homeBrandPartnerInfos.get(position);
        holder.functionName.setText(homeBrandPartnerInfo.getName());
        Glide.with(context).load(homeBrandPartnerInfo.getImgUrl()).into(holder.functionIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, homeBrandPartnerInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeBrandPartnerInfos.size();
    }

    static class HomeBrandPartnerViewHolder extends RecyclerView.ViewHolder {

        ImageView functionIcon;
        TextView functionName;

        HomeBrandPartnerViewHolder(@NonNull View itemView) {
            super(itemView);
            functionIcon = itemView.findViewById(R.id.function_icon);
            functionName = itemView.findViewById(R.id.function_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, HomeBrandPartnerInfo info);
    }


}
