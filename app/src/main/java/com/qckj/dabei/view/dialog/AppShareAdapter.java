package com.qckj.dabei.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qckj.dabei.R;
import com.qckj.dabei.model.mine.AppShareInfo;
import com.qckj.dabei.util.inject.FindViewById;
import com.qckj.dabei.util.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AppShareAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<AppShareInfo> shareInfos;

    public AppShareAdapter(int[] imageId, String[] titles, int[] platformTypes, Context context) {
        shareInfos = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < imageId.length; i++) {
            AppShareInfo info = new AppShareInfo(imageId[i], titles[i], platformTypes[i]);
            shareInfos.add(info);
        }
    }

    @Override
    public int getCount() {
        if (shareInfos != null) {
            return shareInfos.size();
        } else {
            return 0;
        }
    }

    @Override
    public AppShareInfo getItem(int position) {
        return shareInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dialog_app_share_gr_item, parent, false);
            viewHolder = new ViewHolder();
            ViewInject.inject(viewHolder, convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageResource(shareInfos.get(position).getImageId());
        viewHolder.title.setText(shareInfos.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        @FindViewById(R.id.dialog_article_share_gr_item_image)
        public ImageView image;
        @FindViewById(R.id.dialog_app_share_gr_item_title)
        public TextView title;
    }
}
