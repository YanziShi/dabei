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
import com.qckj.dabei.model.home.HomeFunctionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangzhizhong on 2019/3/23.
 */
public class HomeFunctionAdapter extends RecyclerView.Adapter<HomeFunctionAdapter.HomeFunctionViewHolder> {

    private Context context;
    private List<HomeFunctionInfo> homeFunctionInfos;
    private OnHomeFunctionClickListener onHomeFunctionClickListener;

    public HomeFunctionAdapter(Context context) {
        this.context = context;
        homeFunctionInfos = new ArrayList<>();
    }

    public void setHomeFunctionInfos(List<HomeFunctionInfo> homeFunctionInfos) {
        this.homeFunctionInfos.clear();
        this.homeFunctionInfos = homeFunctionInfos;
        notifyDataSetChanged();
    }

    public void setOnHomeFunctionClickListener(OnHomeFunctionClickListener onHomeFunctionClickListener) {
        this.onHomeFunctionClickListener = onHomeFunctionClickListener;
    }

    @NonNull
    @Override
    public HomeFunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_function_item_view, parent, false);
        return new HomeFunctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFunctionViewHolder holder, int position) {
        HomeFunctionInfo homeFunctionInfo = homeFunctionInfos.get(position);
        holder.functionName.setText(homeFunctionInfo.getName());
        if(homeFunctionInfo.getImgUrl()!=null)Glide.with(context).load(homeFunctionInfo.getImgUrl()).into(holder.functionIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onHomeFunctionClickListener != null) {
                    onHomeFunctionClickListener.onFunctionClick(homeFunctionInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeFunctionInfos.size();
    }

    static class HomeFunctionViewHolder extends RecyclerView.ViewHolder {

        ImageView functionIcon;
        TextView functionName;

        HomeFunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            functionIcon = itemView.findViewById(R.id.function_icon);
            functionName = itemView.findViewById(R.id.function_name);
        }
    }

    public interface OnHomeFunctionClickListener {
        void onFunctionClick(HomeFunctionInfo info);
    }

}
