package com.qckj.dabei.ui.lib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qckj.dabei.R;
import com.qckj.dabei.app.SimpleBaseAdapter;
import com.qckj.dabei.util.inject.FindViewById;

/**
 * Created by yangzhizhong on 2019/5/23.
 */
public class ImagesAdapter extends SimpleBaseAdapter<String , ImagesAdapter.ViewHolder> {
    Context context;
    public ImagesAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_image;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, String s, int position) {
        Glide.with(context).load(s).into(viewHolder.imageView);
    }

    @NonNull
    @Override
    protected ImagesAdapter.ViewHolder onNewViewHolder() {
        return new ImagesAdapter.ViewHolder();
    }

    static class ViewHolder {
        @FindViewById(R.id.image)
        private ImageView imageView;

    }
}
