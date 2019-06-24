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
import com.qckj.dabei.model.home.HomeBoutiqueRecommendInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 精品推荐适配器
 * <p>
 * Created by yangzhizhong on 2019/3/25.
 */
public class HomeBusinessActAdapter extends RecyclerView.Adapter<HomeBusinessActAdapter.HomeBoutiqueRecommendViewHolder> {

    private Context context;
    private List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos;
    private OnItemClickListener onItemClickListener;

    public HomeBusinessActAdapter(Context context) {
        this.context = context;
        homeBoutiqueRecommendInfos = new ArrayList<>();
    }

    public void setHomeBoutiqueRecommendInfos(List<HomeBoutiqueRecommendInfo> homeBoutiqueRecommendInfos) {
        this.homeBoutiqueRecommendInfos.clear();
        this.homeBoutiqueRecommendInfos = homeBoutiqueRecommendInfos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HomeBoutiqueRecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_boutique_recommend_item_view, parent, false);
        return new HomeBoutiqueRecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBoutiqueRecommendViewHolder holder, int position) {
        HomeBoutiqueRecommendInfo data = homeBoutiqueRecommendInfos.get(position);
        holder.textTitle.setText(data.getTitle());
        holder.textContent.setText(data.getIntroduce());
        holder.textTime.setText(data.getEndTime()+"-"+data.getEndTime());

        Glide.with(context).load(data.getPhoto()).into(holder.imageIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeBoutiqueRecommendInfos.size();
    }

    static class HomeBoutiqueRecommendViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIcon;
        TextView textTitle;
        TextView textContent;
        TextView textTime;

        HomeBoutiqueRecommendViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.function_icon);
            textTitle = itemView.findViewById(R.id.text_title);
            textContent = itemView.findViewById(R.id.text_content);
            textTime = itemView.findViewById(R.id.text_time);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(HomeBoutiqueRecommendInfo info);
    }


}
