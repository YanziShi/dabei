package com.qckj.dabei.app;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器的基类
 * <p>
 * Created by yangzhizhong on 2019/3/23.
 */
public abstract class SimpleBaseAdapter<Data, ViewHolder> extends BaseAdapter {
    protected Context context;
    private List<Data> dataList = new ArrayList<>();
    protected OnAdapterItemClickListener<Data> onAdapterItemClickListener;

    public SimpleBaseAdapter(Context context) {
        this.context = context;
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener<Data> onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }

    public void addData(List<Data> datas) {
        this.dataList.addAll(datas);
        notifyDataSetChanged();
    }

    public List<Data> getData() {
        return new ArrayList<>(dataList);
    }

    public void setData(List<Data> datas) {
        this.dataList.clear();
        addData(datas);
    }

    public void setDataNull(){
        this.dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Data getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = onNewViewHolder();
            convertView = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
            ViewInject.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Data data = getItem(position);
        if (onAdapterItemClickListener != null) {
            convertView.setOnClickListener(new OnItemClickListener(data, position));
        }
        bindView(viewHolder, data, position);
        return convertView;
    }

    /**
     * 获得资源文件的ID
     *
     * @return 资源文件id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void bindView(ViewHolder viewHolder, Data data, int position);

    protected Context getContext() {
        return context;
    }

    @NonNull
    protected abstract ViewHolder onNewViewHolder();

    public interface OnAdapterItemClickListener<Data> {
        void onAdapterItemClick(int position, Data data);
    }

    private class OnItemClickListener implements View.OnClickListener {
        Data data;
        int position;

        public OnItemClickListener(Data data, int position) {
            this.data = data;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onAdapterItemClickListener != null) {
                onAdapterItemClickListener.onAdapterItemClick(position, data);
            }
        }
    }

    protected Listener mListener;
    public interface Listener {
        void isChange();
    }
    public void setListener(Listener listener){
        mListener = listener;
    }
}
