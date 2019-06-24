package com.qckj.dabei.ui.mine.complain.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qckj.dabei.R;
import com.qckj.dabei.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 意见反馈图片适配器
 * <p>
 * Created by WangYu on 2016/10/19.
 */
public class SuggestionImageAdapter extends RecyclerView.Adapter<SuggestionImageAdapter.MyViewHolder> {
    private ArrayList<ImageInfo> dataList;
    private Context context;
    private Callback callback;
    private ImageInfo addPictureItem; //固定于datalist尾部的“添加图片”项
    private int maxItemCount;

    public SuggestionImageAdapter(Context context, int maxItemCount) {
        this.context = context;
        this.maxItemCount = maxItemCount;
        initData();
    }

    private void initData() {
        this.dataList = new ArrayList<>(maxItemCount + 1);
        addPictureItem = new ImageInfo();
        addPictureItem.isAddPictureItem = true;
        this.dataList.add(addPictureItem);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.complain_image_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (dataList.get(position).isAddPictureItem) {
            holder.deleteView.setVisibility(View.GONE);
            holder.imageView.setImageResource(R.mipmap.ic_upload);
        } else {
            holder.deleteView.setVisibility(View.VISIBLE);
            GlideUtil.displayImage(context, dataList.get(position).imagePath, holder.imageView, R.mipmap.ic_empty_data);
        }

        holder.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size() - position);

                if (!dataList.contains(addPictureItem)) {
                    dataList.add(addPictureItem);
                    notifyItemInserted(dataList.size());
                }
                if (dataList.size() == 1) {
                    if (callback != null) {
                        callback.empty();
                    }
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >= dataList.size()) {
                    return;
                }
                if (dataList.get(position).isAddPictureItem) {
                    if (callback != null) {
                        callback.optionClick();
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void addItem(ImageInfo imageItem) {

        if (getItemCount() >= maxItemCount + 1) {
            //添加个数已经达到最大个数maxItemCount,无法再添加
            return;
        }

        if (callback != null) {
            callback.added(imageItem);
        }

        if (dataList.size() > 0) {
            dataList.add(dataList.size() - 1, imageItem);
            if (dataList.size() == maxItemCount + 1) {
                dataList.remove(addPictureItem);
            }
            notifyDataSetChanged();
        }

    }

    //获取被添加的图片数据列表
    public List<ImageInfo> getData() {
        List<ImageInfo> tempList = new ArrayList<>(dataList.size());
        tempList.addAll(dataList);
        tempList.remove(addPictureItem);
        return tempList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        /** 点击了图片 */
//        void imageClick();

        /**
         * 点击了选项
         */
        void optionClick();

        /**
         * 一个ImageItem被添加
         */
        void added(ImageInfo item);

        /**
         * 无数据
         */
        void empty();
    }

    public static class ImageInfo {
        @NonNull
        public String imagePath = "";
        @NonNull
        public String imageId = "";

        public boolean isAddPictureItem = false; //是否为添加图片项
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView deleteView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.get_iv);
            deleteView = itemView.findViewById(R.id.sub_iv);
        }
    }
}
